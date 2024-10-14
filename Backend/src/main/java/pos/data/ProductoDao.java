package pos.data;


import pos.logic.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDao {
    Database db;

    public ProductoDao() {
        db = Database.instance();
    }

    public void create(Producto e) throws Exception {
        String sql = "insert into " +
                "Producto " +
                "(codigo ,descripcion, unidadMedida,precioUnitario,existencia,categoria) " +
                "values(?,?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        stm.setString(2, e.getDescripcion());
        stm.setString(3, e.getUnidadDeMedidad());
        stm.setDouble(4, e.getPrecioUnitario());
        stm.setInt(5,e.getExistencia());
        stm.setString(6, e.getCategoria().getIdCategoria());
        db.executeUpdate(stm);
    }

    public Producto read(String codigo) throws Exception {
        String sql = "select " +
                "* " +
                "from  Producto t " +
                "inner join Categoria c on t.categoria=c.id " +
                "where t.codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);
        CategoriaDao categoriaDao=new CategoriaDao();
        if (rs.next()) {
            Producto r = from(rs, "t");
            r.setCategoria(categoriaDao.from(rs, "c"));
            return r;
        } else {
            throw new Exception("Producto NO EXISTE");
        }
    }

    public void update(Producto e) throws Exception {
        String sql = "update " +
                "Producto " +
                "set descripcion=?, unidadMedida=?, precioUnitario=?,existencia=?, categoria=? " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getDescripcion());
        stm.setString(2, e.getUnidadDeMedidad());
        stm.setDouble(3, e.getPrecioUnitario());
        stm.setDouble(4, e.getExistencia());
        stm.setString(5, e.getCategoria().getIdCategoria());
        stm.setString(6, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Producto NO EXISTE");
        }

    }

    // Método para actualizar las existencias del producto en la base de datos
    public void updateExistencias(Producto producto) throws Exception {
        String sql = "UPDATE Producto SET existencia = ? WHERE codigo = ?";

        try (PreparedStatement stmt = Database.instance().prepareStatement(sql)) {
            stmt.setInt(1, producto.getExistencia()); // Nueva cantidad de existencias
            stmt.setString(2, producto.getCodigo());  // Código del producto que quieres actualizar

            Database.instance().executeUpdate(stmt); // Ejecutar el update
        }catch (Exception e){

        }

    }

    public void delete(Producto e) throws Exception {
        String sql = "delete " +
                "from Producto " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Producto NO EXISTE");
        }
    }

    public List<Producto> search(Producto e) throws Exception {
        List<Producto> resultado = new ArrayList<Producto>();
        String sql = "select * " +
                "from " +
                "Producto t " +
                "inner join Categoria c on t.categoria=c.id " +
                "where t.codigo like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getCodigo() + "%");
        ResultSet rs = db.executeQuery(stm);
        CategoriaDao categoriaDao=new CategoriaDao();
        while (rs.next()) {
            Producto r = from(rs, "t");
            r.setCategoria(categoriaDao.from(rs, "c"));
            resultado.add(r);
        }
        return resultado;
    }

    public Producto from(ResultSet rs, String alias) throws Exception {
        Producto e = new Producto();
        e.setCodigo(rs.getString(alias + ".codigo"));
        e.setDescripcion(rs.getString(alias + ".descripcion"));
        e.setUnidadDeMedidad(rs.getString(alias + ".unidadMedida"));
        e.setPrecioUnitario(rs.getFloat(alias + ".precioUnitario"));
        e.setExistencia(rs.getInt(alias + ".existencia"));

        return e;
    }

}
