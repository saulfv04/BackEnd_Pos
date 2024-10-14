package pos.data;

import pos.logic.Cajero;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CajeroDao {
    Database db;

    public CajeroDao() {
        db = Database.instance();
    }


    public List<Cajero> getAll() throws Exception {
        List<Cajero> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Cajero"; // Consulta para obtener todas las categorías
        try (PreparedStatement stmt = Database.instance().prepareStatement(sql);
             ResultSet rs = Database.instance().executeQuery(stmt)){
            while (rs.next()) {
                Cajero cajero= from(rs, "t"); // Asumiendo que tienes un método `from`
                resultado.add(cajero);
            }


        }
        return resultado;
    }

    // Método para crear un nuevo Cajero en la base de datos
    public void create(Cajero e) throws Exception {
        String sql = "insert into " +
                "Cajero " +
                "(id, nombre) " +
                "values(?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getNombre());
        db.executeUpdate(stm);
    }

    // Método para leer un Cajero a partir de su id
    public Cajero read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from Cajero t " +
                "where t.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "t");
        } else {
            throw new Exception("Cajero NO EXISTE");
        }
    }

    // Método para actualizar un Cajero
    public void update(Cajero e) throws Exception {
        String sql = "update " +
                "Cajero " +
                "set nombre=? " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNombre());
        stm.setString(2, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cajero NO EXISTE");
        }
    }

    // Método para eliminar un Cajero
    public void delete(Cajero e) throws Exception {
        String sql = "delete " +
                "from Cajero " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cajero NO EXISTE");
        }
    }

    // Método para buscar Cajeros que coincidan con un criterio (por ejemplo, nombre)
    public List<Cajero> search(Cajero e) throws Exception {
        List<Cajero> resultado = new ArrayList<Cajero>();
        String sql = "select * " +
                "from Cajero t " +
                "where t.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            Cajero r = from(rs, "t");
            resultado.add(r);
        }
        return resultado;
    }

    // Método para mapear el ResultSet a un objeto Cajero
    public Cajero from(ResultSet rs, String alias) throws Exception {
        Cajero e = new Cajero();
        e.setId(rs.getString(alias + ".id"));
        e.setNombre(rs.getString(alias + ".nombre"));
        return e;
    }
}
