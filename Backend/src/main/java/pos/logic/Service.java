package pos.logic;

import pos.data.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Service implements IService{
    private static Service theInstance;
    public static IService instance;

    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private ClienteDao clienteDao;
    private CajeroDao cajeroDao;
    private FacturaDao facturaDao;
    private LineaDao lineaDao;
    private UsuarioDao usuarioDao;

    public Service(){
        try{
            categoriaDao= new CategoriaDao();
            productoDao= new ProductoDao();
            clienteDao= new ClienteDao();
            cajeroDao= new CajeroDao();
            facturaDao= new FacturaDao();
            lineaDao= new LineaDao();
            usuarioDao= new UsuarioDao();
        }catch (Exception e){

        }
    }

    @Override
    public void create(Producto producto) throws Exception {
        // Lógica para crear un nuevo producto
        try {
            productoDao.create(producto); // Utiliza el DAO para crear el producto en la base de datos
        } catch (Exception ex) {
            throw new Exception("Error al crear el producto", ex);
        }
    }

    @Override
    public Producto read(Producto producto) throws Exception {
        // Lógica para leer un producto
        try {
            return productoDao.read(producto.getCodigo()); // Utiliza el DAO para leer el producto
        } catch (Exception ex) {
            throw new Exception("Error al leer el producto", ex);
        }
    }

    @Override
    public void update(Producto producto) throws Exception {
        // Lógica para actualizar un producto
        try {
            productoDao.update(producto); // Utiliza el DAO para actualizar el producto
        } catch (Exception ex) {
            throw new Exception("Error al actualizar el producto", ex);
        }
    }

    @Override
    public void delete(Producto producto) throws Exception {
        // Lógica para eliminar un producto
        try {
            productoDao.delete(producto); // Utiliza el DAO para eliminar el producto
        } catch (Exception ex) {
            throw new Exception("Error al eliminar el producto", ex);
        }
    }

    @Override
    public void updateExistencias(Producto producto) throws Exception {
        // Lógica para actualizar existencias de un producto
        try {
            productoDao.updateExistencias(producto); // Método que debes implementar en el DAO
        } catch (Exception ex) {
            throw new Exception("Error al actualizar existencias del producto", ex);
        }
    }

    @Override
    public List<Producto> search(Producto producto) {
        // Lógica para buscar productos
        try {
            return productoDao.search(producto); // Utiliza el DAO para buscar productos
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar productos", ex);
        }
    }

    @Override
    public List<Categoria> search(Categoria categoria) {
        // Lógica para buscar categorías
        try {
            return categoriaDao.search(categoria); // Utiliza el DAO para buscar categorías
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar categorías", ex);
        }
    }

    @Override
    public List<Categoria> getCategorias() {
        try {
            return categoriaDao.getAll(); // Obtiene la lista de categorías
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener categorías", ex);
        }
    }

    @Override
    public void create(Factura factura) throws Exception {
        // Lógica para crear una nueva factura
        try {
            facturaDao.create(factura); // Utiliza el DAO para crear la factura en la base de datos
        } catch (Exception ex) {
            throw new Exception("Error al crear la factura", ex);
        }
    }

    @Override
    public Factura read(Factura factura) throws Exception {
        // Lógica para leer una factura
        try {
            return facturaDao.read(factura.getId()); // Utiliza el DAO para leer la factura
        } catch (Exception ex) {
            throw new Exception("Error al leer la factura", ex);
        }
    }

    @Override
    public void update(Factura factura) throws Exception {
        // Lógica para actualizar una factura
        try {
            facturaDao.update(factura); // Utiliza el DAO para actualizar la factura
        } catch (Exception ex) {
            throw new Exception("Error al actualizar la factura", ex);
        }
    }

    @Override
    public void delete(Factura factura) throws Exception {
        // Lógica para eliminar una factura
        try {
            facturaDao.delete(factura); // Utiliza el DAO para eliminar la factura
        } catch (Exception ex) {
            throw new Exception("Error al eliminar la factura", ex);
        }
    }

    @Override
    public List<Factura> search(Factura factura) {
        try {
            // Llama al método del DAO que busca clientes
            return facturaDao.search(factura);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar facturas", ex);
        }
    }

    @Override
    public List<Linea> searchByFacturId(String facturaId) {
        try {
            // Llama al método del DAO que busca líneas por ID de factura
            return lineaDao.searchByFacturaId(facturaId);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar líneas de la factura con ID: " + facturaId, ex);
        }
    }

    @Override
    public void create(Cliente cliente) throws Exception {
        try {
            // Aquí llamas al método del DAO que obtiene las cliente
            clienteDao.create(cliente);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener cliente", ex);
        }
    }

    @Override
    public Cliente read(Cliente cliente) throws Exception {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("El cliente y su ID no pueden ser nulos");
        }
        try {
            return clienteDao.read(cliente.getId()); // Llama al DAO para leer el cliente
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer el cliente", ex);
        }
    }

    @Override
    public void update(Cliente cliente) throws Exception {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("El cliente y su ID no pueden ser nulos");
        }
        try {
            clienteDao.update(cliente); // Llama al DAO para actualizar el cliente
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar el cliente", ex);
        }
    }

    @Override
    public void delete(Cliente cliente) throws Exception {
        if (cliente == null || cliente.getId() == null) {
            throw new IllegalArgumentException("El cliente y su ID no pueden ser nulos");
        }
        try {
            clienteDao.delete(cliente); // Llama al DAO para eliminar el cliente
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar el cliente", ex);
        }
    }

    @Override
    public List<Cliente> search(Cliente cliente) {
        try {
            // Llama al método del DAO que busca clientes
            return clienteDao.search(cliente);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar clientes", ex);
        }
    }

    @Override
    public void create(Linea linea) throws Exception {
        try {
            lineaDao.create(linea); // Llama al método del DAO para crear una nueva línea
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear la línea", ex);
        }
    }

    @Override
    public Linea read(Linea linea) throws Exception {
        try {
            return lineaDao.read(linea.getIdLinea()); // Llama al método del DAO para leer una línea
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer la línea", ex);
        }
    }

    @Override
    public void update(Linea linea) throws Exception {
        try {
            lineaDao.update(linea); // Llama al método del DAO para actualizar la línea
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar la línea", ex);
        }
    }

    @Override
    public void delete(Linea linea) throws Exception {
        try {
            lineaDao.delete(linea); // Llama al método del DAO para eliminar la línea
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar la línea", ex);
        }
    }

    @Override
    public List<Linea> search(Linea linea) {
        try {
            // Llama al método del DAO que busca clientes
            return lineaDao.search(linea);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar clientes", ex);
        }
    }

    @Override
    public void create(Cajero cajero) throws Exception {
        try {
            cajeroDao.create(cajero); // Llama al método del DAO para crear un nuevo cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear el cajero", ex);
        }
    }

    @Override
    public Cajero read(Cajero cajero) throws Exception {
        try {
            return cajeroDao.read(cajero.getId()); // Llama al método del DAO para leer un cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer el cajero", ex);
        }
    }

    @Override
    public void update(Cajero cajero) throws Exception {
        try {
            cajeroDao.update(cajero); // Llama al método del DAO para actualizar el cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar el cajero", ex);
        }
    }

    @Override
    public void delete(Cajero cajero) throws Exception {
        try {
            cajeroDao.delete(cajero); // Llama al método del DAO para eliminar el cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar el cajero", ex);
        }
    }

    @Override
    public List<Cajero> search(Cajero cajero) {
        try {
            // Llama al método del DAO que busca clientes
            return cajeroDao.search(cajero);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar cajeros", ex);
        }
    }

    @Override
    public void create(Usuarios usuarios) throws Exception {
        try {
             usuarioDao.create(usuarios); // Llama al método del DAO para crear un nuevo cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al crear el usuario", ex);
        }

    }

    @Override
    public Usuarios read(Usuarios usuarios) throws Exception {
        try {
            return usuarioDao.read(usuarios.getId()); // Llama al método del DAO para leer un cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al leer el usuario", ex);
        }
    }

    @Override
    public void delete(Usuarios usuarios) throws Exception {
        try {
            usuarioDao.delete(usuarios); // Llama al método del DAO para eliminar el cajero
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar el usuario", ex);
        }
    }

    @Override
    public List<Usuarios> search(Usuarios usuarios) {
        try {
            // Llama al método del DAO que busca clientes
            return usuarioDao.search(usuarios);
        } catch (Exception ex) {
            throw new RuntimeException("Error al buscar usuario", ex);
        }
    }

    public float[][] estadisticas(List<Categoria> rows, List<String> cols, Rango rango) throws Exception {
        try {
            return lineaDao.estadisticas(rows, cols, rango); // Llama al método del DAO que calcula las estadísticas
        } catch (Exception ex) {
            throw new RuntimeException("Error al calcular las estadísticas", ex);
        }
    }

    @Override
    public void exit() {
        System.out.println("saliendo...");
    }


    @Override
    public void deliver_message(String s) {

    }
}

