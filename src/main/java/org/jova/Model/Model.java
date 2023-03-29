package org.jova.Model;

import org.jova.AES256;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.*;
import java.util.Properties;

public class Model {
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    private boolean optionCanceled;
    private int id;
    private File archivoConfig;
    private String rutaBaseDatos;
    private String key;

    public void crearBaseDatos() {
        //Si cancela la seleccion de archivo, mandar para el panel principal nuevamente
        Properties propiedades = new Properties();
        String rutaArchivoConfig = "config.properties";
        try {
            archivoConfig = new File(rutaArchivoConfig);
            if (archivoConfig.exists()) {
                FileInputStream archivoEntrada = new FileInputStream(archivoConfig);
                propiedades.load(archivoEntrada);
                archivoEntrada.close();
            }
            rutaBaseDatos = propiedades.getProperty("rutaBaseDatos");
            if (rutaBaseDatos == null || rutaBaseDatos.isEmpty()) {
                rutaBaseDatos = seleccionarArchivoBaseDatos();
                propiedades.setProperty("rutaBaseDatos", rutaBaseDatos);
                FileOutputStream archivoSalida = new FileOutputStream(rutaArchivoConfig);
                propiedades.store(archivoSalida, "Configuración de la aplicación");
                archivoSalida.close();
            }
            crearTablaUsuarios(rutaBaseDatos);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de configuración: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla de usuarios: " + e.getMessage());
        }
    }

    private String seleccionarArchivoBaseDatos() {
        rutaBaseDatos = "";
        JFileChooser selectorArchivo = new JFileChooser();
        int seleccion = selectorArchivo.showSaveDialog(null);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            rutaBaseDatos = selectorArchivo.getSelectedFile().getAbsolutePath();
            if (!rutaBaseDatos.endsWith(".db")) {
                optionCanceled = false;
                rutaBaseDatos += ".db";
            }
        }else if(seleccion == JFileChooser.CANCEL_OPTION){
            optionCanceled = true;
            System.out.println("Option Canceled");
        }
        return rutaBaseDatos;
    }

    private void crearTablaUsuarios(String rutaBaseDatos) throws SQLException {
        Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos);
        Statement statement = conexion.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, title TEXT, email TEXT, password TEXT)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS masterkey (id INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT, key TEXT)");
        //Arreglar para que salgan las columas de la JTable cuando se crea la base de datos
        try {
            statement.close();
            conexion.close();
        }catch (SQLException e){
            System.out.println("Error at crearTablaUsuarios" + e);
        }
    }

    private Connection obtenerConexion() {
        Connection con = null;
        try {
            Properties propiedades = new Properties();
            FileInputStream archivoEntrada = new FileInputStream("config.properties");
            propiedades.load(archivoEntrada);
            archivoEntrada.close();
            rutaBaseDatos = propiedades.getProperty("rutaBaseDatos");
            con = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos);
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de configuración: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return con;
    }

    public void insertDataInDB(String title, String email, String password, JTable table, DefaultTableModel model) throws Exception {
        connection = obtenerConexion();
        connection.setAutoCommit(false);
        String sql = "insert into accounts(title, email, password) values(?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, email);
            password = AES256.encrypt(password, key);
            preparedStatement.setString(3, password);
            preparedStatement.execute();
            connection.commit();
            System.out.println("Transaction Commited");

            String idSQL = "select id from accounts where title = ? and email = ? and password = ?";
            preparedStatement = connection.prepareStatement(idSQL);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);
            id = preparedStatement.executeQuery().getInt(1);
            password = AES256.decrypt(password, key);
            model.addRow(new Object[]{id,title, email, password});
            preparedStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
            if(connection != null){
                try{
                    connection.rollback();
                    System.out.println("Transaction is being rolled back");
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }finally {
            cerrarConexion(connection);
        }

    }

    public void insertKeyInDB(String keyHash) throws SQLException {
        connection = obtenerConexion();
        connection.setAutoCommit(false);
        String sql = "insert into masterkey(id, key) values (?, ?)";
        try{
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            keyHash = generateHash(keyHash);
            preparedStatement.setString(2, keyHash);
            preparedStatement.execute();
            connection.commit();
            preparedStatement.close();

        }catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
        }finally {
            cerrarConexion(obtenerConexion());
        }
    }

    public String getKeyHashFromDB() throws SQLException {
        connection = obtenerConexion();
        String sql = "select key from masterkey where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, 1);
        resultSet = preparedStatement.executeQuery();
        String key = null;
        if(resultSet.next()){
            key = resultSet.getString("key");
            try {
                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                obtenerConexion();
            }
            return key;
        }
        try {
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            cerrarConexion(obtenerConexion());
        }
        return null;
    }

    public void scanDatabase(JTable table, DefaultTableModel tableModel) throws Exception {
        table.setModel(tableModel);
        connection = obtenerConexion();
        String query = "select * from accounts";
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        while(resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            password = AES256.decrypt(password, key);
            tableModel.addRow(new Object[]{id,title, email, password});
        }
            statement.close();
            resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            cerrarConexion(obtenerConexion());
        }
    }

    public void updateDataOfDB(int rowId, String title, String email, String password) throws Exception {
        connection = obtenerConexion();
        connection.setAutoCommit(false);
        String sql = "update accounts set title = ?, email = ?, password = ? where id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, email);
            password = AES256.encrypt(password, key);
            preparedStatement.setString(3, password);
            preparedStatement.setInt(4, rowId);
            preparedStatement.executeUpdate();
            connection.commit();
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
            System.out.println("Transaction Rolled Back");
        }finally {
            cerrarConexion(obtenerConexion());
        }
    }

    public void deleteDataOfDB(int row) throws SQLException, NoSuchAlgorithmException {
        connection = obtenerConexion();
        connection.setAutoCommit(false);
        String sql = "delete from accounts where id = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, row);
            preparedStatement.executeUpdate();
            connection.commit();
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
            connection.rollback();
            System.out.println("Transaction Rolled Back");
        }finally {
            cerrarConexion(obtenerConexion());
        }
    }

    public String getTitleFromDB(int rowId) throws SQLException {
        connection = obtenerConexion();
        String sql = "select title from accounts where id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, rowId);
        resultSet = preparedStatement.executeQuery();
        String title = null;
        if(resultSet.next()){
            title = resultSet.getString("title");
            try {
                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                cerrarConexion(obtenerConexion());
            }
            return title;
        }
        try {
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            cerrarConexion(obtenerConexion());
        }
        return null;
    }

    public String getEmailFromDB(int rowId) throws SQLException, NoSuchAlgorithmException {
        connection = obtenerConexion();
        String sql = "select email from accounts where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, rowId);
        resultSet = preparedStatement.executeQuery();
        String email = null;
        if(resultSet.next()){
            email = resultSet.getString("email");
            try {
                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                cerrarConexion(obtenerConexion());
            }
            return email;
        }
        try {
            resultSet.close();
            preparedStatement.close();
            cerrarConexion(obtenerConexion());
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            cerrarConexion(obtenerConexion());
        }
        return null;
    }

    public String getPasswordFromDB(int rowId) throws SQLException {
        connection = obtenerConexion();
        String sql = "select password from accounts where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, rowId);
        resultSet = preparedStatement.executeQuery();
        String password = null;
        if(resultSet.next()){
            password = resultSet.getString("password");
            try {
                resultSet.close();
                preparedStatement.close();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                cerrarConexion(obtenerConexion());
            }
            return password;
        }

        try {
            resultSet.close();
            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            cerrarConexion(obtenerConexion());
        }
        return null;
    }

    public boolean existeRutaBaseDatos() {
        Properties propiedades = new Properties();
        FileInputStream archivoEntrada = null;
        try {
            archivoEntrada = new FileInputStream("config.properties");
            propiedades.load(archivoEntrada);
            archivoEntrada.close();
            String rutaBaseDatos = propiedades.getProperty("rutaBaseDatos");
            return rutaBaseDatos != null && !rutaBaseDatos.isEmpty();
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo de configuración: " + e.getMessage());
            return false;
        } finally {
            if (archivoEntrada != null) {
                try {
                    archivoEntrada.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el archivo de configuración: " + e.getMessage());
                }
            }
        }
    }

    public void getMasterKey(String masterKey){
        this.key = masterKey;
        generateHash(masterKey);
    }

    public String masterKey(){
        return this.key;
    }

    public String generateHash(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    key.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isDbEmpty() throws SQLException {
        try {
            connection = obtenerConexion();
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + rutaBaseDatos);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM accounts");

            if (resultSet.next() && resultSet.getInt(1) == 0) {
                System.out.println("The database is empty.");
                return true;
            } else {
                System.out.println("The database is not empty.");
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean isFileChooserCanceled(){
        return optionCanceled;
    }

    private void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}