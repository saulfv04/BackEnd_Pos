package pos.logic;

import pos.data.*;

import java.util.List;

public class Service implements  IService {
    private static Service theInstance;
    public static Service instance;

    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;
    private ClienteDao clienteDao;
    private CajeroDao cajeroDao;
    private FacturaDao facturaDao;
    private LineaDao lineaDao;

    public Service() {
        try {
            categoriaDao = new CategoriaDao();
            productoDao = new ProductoDao();
            clienteDao = new ClienteDao();
            cajeroDao = new CajeroDao();
            facturaDao = new FacturaDao();
            lineaDao = new LineaDao();
        } catch (Exception e) {

        }
    }

    @Override
    public void create(Producto producto) throws Exception {

    }

    @Override
    public Producto read(Producto producto) throws Exception {
        return null;
    }

    @Override
    public void update(Producto producto) throws Exception {

    }

    @Override
    public void delete(Producto producto) throws Exception {

    }

    @Override
    public List<Producto> search(Producto producto) {
        return List.of();
    }

    @Override
    public List<Categoria> search(Categoria categoria) {
        return List.of();
    }

    @Override
    public void create(Factura factura) throws Exception {

    }

    @Override
    public Factura read(Factura factura) throws Exception {
        return null;
    }

    @Override
    public void update(Factura factura) throws Exception {

    }

    @Override
    public void delete(Factura factura) throws Exception {

    }

    @Override
    public List<Factura> search(Factura factura) {
        return List.of();
    }

    @Override
    public void create(Cliente cliente) throws Exception {

    }

    @Override
    public Cliente read(Cliente cliente) throws Exception {
        return null;
    }

    @Override
    public void update(Cliente cliente) throws Exception {

    }

    @Override
    public void delete(Cliente cliente) throws Exception {

    }

    @Override
    public List<Cliente> search(Cliente cliente) {
        return List.of();
    }

    @Override
    public void create(Linea linea) throws Exception {

    }

    @Override
    public Linea read(Linea linea) throws Exception {
        return null;
    }

    @Override
    public void update(Linea linea) throws Exception {

    }

    @Override
    public void delete(Linea linea) throws Exception {

    }

    @Override
    public List<Linea> search(Linea linea) {
        return List.of();
    }

    @Override
    public void create(Cajero cajero) throws Exception {

    }

    @Override
    public Cajero read(Cajero cajero) throws Exception {
        return null;
    }

    @Override
    public void update(Cajero cajero) throws Exception {

    }

    @Override
    public void delete(Cajero cajero) throws Exception {

    }

    @Override
    public List<Cajero> search(Cajero cajero) {
        return List.of();
    }
}

