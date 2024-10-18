package pos.data;

import pos.logic.Categoria;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao {
    Database db;

    public CategoriaDao() {
        db = Database.instance();
    }

    public List<Categoria> getAll() throws Exception {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categoria"; // Asegúrate de que 'id' está presente en la tabla
        try (PreparedStatement stmt = Database.instance().prepareStatement(sql);
             ResultSet rs = Database.instance().executeQuery(stmt)) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getString("id")); // Asegúrate de que la columna 'id' existe
                categoria.setNombre(rs.getString("nombre"));
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    public List<Categoria> search(Categoria e) throws Exception {
        List<Categoria> resultado = new ArrayList<Categoria>();
        String sql = "select * " +
                "from " +
                "Categoria t " +
                "where t.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            Categoria r= from(rs, "t");
            resultado.add(r);
        }
        return resultado;
    }


    public Categoria from(ResultSet rs, String alias) throws Exception {
        Categoria e = new Categoria();
        e.setIdCategoria(rs.getString(alias + ".id"));
        e.setNombre(rs.getString(alias + ".nombre"));
        return e;
    }
}
