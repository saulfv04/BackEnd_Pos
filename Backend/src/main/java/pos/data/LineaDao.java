package pos.data;


import pos.logic.Categoria;
import pos.logic.Factura;
import pos.logic.Linea;
import pos.logic.Producto;
import pos.logic.Rango;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LineaDao {
    Database db;

    public LineaDao() {
        db = Database.instance();
    }

    // Método para crear una nueva Linea en la base de datos
    public void create(Linea e) throws Exception {
        String sql = "insert into " +
                "Linea " +
                "(id, productoId, cantidad, descuento, facturaId) " +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getIdLinea());
        stm.setString(2, e.getProducto().getCodigo());  // Se asume que Producto tiene un campo 'codigo'
        stm.setInt(3, e.getCantidad());
        stm.setDouble(4, e.getDescuento());
        stm.setString(5, e.getFacturaId());
        db.executeUpdate(stm);
    }

    // Método para leer una Linea a partir de su idLinea
    public Linea read(String idLinea) throws Exception {
        String sql = "select " +
                "* " +
                "from Linea t " +
                "inner join Producto p on t.producto = p.codigo " +
                "where t.idLinea=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, idLinea);
        ResultSet rs = db.executeQuery(stm);
        ProductoDao productoDao = new ProductoDao();
        if (rs.next()) {
            Linea r = from(rs, "t");
            r.setProducto(productoDao.read(rs.getString("p.codigo")));  // Lee el producto asociado
            return r;
        } else {
            throw new Exception("Linea NO EXISTE");
        }
    }

    public float[][] estadisticas(List<Categoria> rows, List<String> cols, Rango rango) throws Exception {
        float[][] resultado = new float[rows.size()][cols.size()];
        if (rows.size() == 0) return resultado;
        String sql = "select " + "c.id as categoria, " +
                "CONCAT(year(f.fecha),'-' , LPAD(month(f.fecha),2,0)) as periodo, " +
                "sum(l.cantidad*p.precioUnitario*(1-l.descuento/100)) as total " +
                "from linea l " +
                "inner join factura f on l.facturaId = f.id " +
                "inner join producto p on l.productoId = p.codigo " +
                "inner join categoria c on p.categoria = c.id " +
                "where year(f.fecha) >= ? " +
                "and month(fecha) >= ? " +
                "and year(f.fecha)<=? " +
                "and month(fecha)<=? "+
                "and c.id in("+",?".repeat(rows.size()).substring(1)+") "+
                "group by categoria, periodo;";

        PreparedStatement stm = db.prepareStatement(sql);
        stm.setInt(1,rango.getAnioInicio());
        stm.setInt(2,rango.getMesInicio());
        stm.setInt(3,rango.getAnioFinal());
        stm.setInt(4,rango.getMesFinal());
        for(int i = 0;i<rows.size();i++){
            stm.setString(5+i,rows.get(i).getIdCategoria());
        }
        ResultSet rs = db.executeQuery(stm);
        ProductoDao productoDao = new ProductoDao();
        int row = 0;
        int col = 0;
        while(rs.next()){
            row = rows.indexOf(new Categoria(rs.getString("categoria")));
            col = cols.indexOf(rs.getString("periodo"));
            resultado[row][col] = rs.getFloat("total");
        }
        return resultado;
    }




    // Método para actualizar una Linea
    public void update(Linea e) throws Exception {
        String sql = "update " +
                "Linea " +
                "set producto=?, cantidad=?, descuento=?, facturaId=? " +
                "where idLinea=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getProducto().getCodigo());  // Se actualiza el producto
        stm.setInt(2, e.getCantidad());
        stm.setDouble(3, e.getDescuento());
        stm.setString(4, e.getFacturaId());
        stm.setString(5, e.getIdLinea());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Linea NO EXISTE");
        }
    }

    // Método para eliminar una Linea
    public void delete(Linea e) throws Exception {
        String sql = "delete " +
                "from Linea " +
                "where idLinea=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getIdLinea());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Linea NO EXISTE");
        }
    }

    // Método para buscar Lineas que coincidan con un criterio (por ejemplo, facturaId)
    public List<Linea> search(Linea e) throws Exception {
        List<Linea> resultado = new ArrayList<>();
        String sql = "select * " +
                "from Linea t " +
                "inner join Producto p on t.producto = p.codigo " +
                "where t.facturaId like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getFacturaId() + "%");
        ResultSet rs = db.executeQuery(stm);
        ProductoDao productoDao = new ProductoDao();
        while (rs.next()) {
            Linea r = from(rs, "t");
            r.setProducto(productoDao.read(rs.getString("p.codigo")));  // Agrega el producto asociado
            resultado.add(r);
        }
        return resultado;
    }


    // En LineaDao
    public List<Linea> searchByFacturaId(String facturaId) throws Exception {
        List<Linea> resultado = new ArrayList<>();
        String sql = "select * from Linea where facturaId=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, facturaId);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Linea linea = from(rs);
            resultado.add(linea);
        }
        return resultado;
    }

    // Método para mapear un ResultSet a un objeto Linea
    public Linea from(ResultSet rs) throws Exception {
        Linea linea = new Linea();
        ProductoDao productoDao = new ProductoDao();
        linea.setIdLinea(rs.getString("id")); // Asumiendo que "idLinea" es el nombre de la columna
        // Aquí puedes mapear otros campos de Linea según tu clase
        linea.setProducto(productoDao.read(rs.getString("productoId")));
        linea.setCantidad(rs.getInt("cantidad"));
        linea.setDescuento(rs.getDouble("descuento"));
        linea.setFacturaId(rs.getString("facturaId")); // Asumiendo que esta columna existe
        return linea;
    }

    // Método para mapear el ResultSet a un objeto Linea
    public Linea from(ResultSet rs, String alias) throws Exception {
        Linea e = new Linea();
        e.setIdLinea(rs.getString(alias + ".id"));
        e.setCantidad(rs.getInt(alias + ".cantidad"));
        e.setDescuento(rs.getDouble(alias + ".descuento"));
        e.setFacturaId(rs.getString(alias + ".facturaId"));
        return e;
    }

    // Método para obtener líneas por factura
    public List<Linea> getByFactura(Factura factura) throws Exception {
        List<Linea> lineas = new ArrayList<>();
        String sql = "SELECT * FROM Linea WHERE facturaId = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, factura.getId());
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()) {
            Linea linea = from(rs);
            lineas.add(linea);
        }

        return lineas;
    }
}
