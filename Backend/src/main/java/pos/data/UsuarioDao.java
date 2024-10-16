package pos.data;

import pos.logic.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    Database db;

    public UsuarioDao() {
        db = Database.instance();
    }


    // Método para crear un nuevo Usuario en la base de datos
    public void create(Usuarios u) throws Exception {
        String sql = "INSERT INTO Usuarios (id, clave) VALUES (?, ?, ?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        stm.setString(2, u.getClave());
        db.executeUpdate(stm);
    }

    // Método para leer un Usuario a partir de su id
    public Usuarios read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from  Usuarios t " +
                "where t.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "t");
        } else {
            throw new Exception("Usario NO EXISTE");
        }
    }

    // Método para eliminar un Usuario
    public void delete(Usuarios u) throws Exception {
        String sql = "DELETE FROM Usuarios WHERE id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, u.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Usuario NO EXISTE");
        }
    }

    // Método para buscar Usuarios que coincidan con un criterio
    public List<Usuarios> search(Usuarios u) throws Exception {
        List<Usuarios> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios t WHERE clave LIKE ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + u.getClave() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            Usuarios r = from(rs, "t");
            resultado.add(r);
        }
        return resultado;
    }

    // Método para mapear el ResultSet a un objeto Usuario
    public Usuarios from(ResultSet rs, String alias) throws Exception {
        Usuarios u = new Usuarios();
        u.setId(rs.getString(alias + ".id"));
        u.setClave(rs.getString(alias + ".clave"));
        return u;
    }
}
