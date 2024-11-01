package pos.data;

import pos.logic.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    Database db;

    public ClienteDao() {
        db = Database.instance();
    }

    public List<Cliente> getAll() throws Exception {
        List<Cliente> resultado = new ArrayList<>();
        String sql = "SELECT * FROM Cliente"; // Consulta para obtener todas las categorías
        try (PreparedStatement stmt = Database.instance().prepareStatement(sql);
             ResultSet rs = Database.instance().executeQuery(stmt)) {
            while (rs.next()) {
                Cliente cliente = from(rs, "t"); // Asumiendo que tienes un método `from`
                resultado.add(cliente);
            }
        }
        return resultado;
    }

    // Método para crear un nuevo Cliente en la base de datos
    public void create(Cliente e) throws Exception {
        String sql = "insert into " +
                "Cliente " +
                "(id, nombre, telefono, email, descuento) " +
                "values(?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getNombre());
        stm.setString(3, e.getTelefono());
        stm.setString(4, e.getEmail());
        stm.setDouble(5, e.getDescuento());
        db.executeUpdate(stm);
    }

    // Método para leer un Cliente a partir de su id
    public Cliente read(String id) throws Exception {
        String sql = "select " +
                "* " +
                "from  Cliente t " +
                "where t.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "t");
        } else {
            throw new Exception("Cliente NO EXISTE");
        }
    }

    // Método para actualizar un Cliente
    public void update(Cliente e) throws Exception {
        String sql = "update " +
                "Cliente " +
                "set nombre=?, telefono=?, email=?, descuento=? " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNombre());
        stm.setString(2, e.getTelefono());
        stm.setString(3, e.getEmail());
        stm.setDouble(4, e.getDescuento());
        stm.setString(5, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cliente NO EXISTE");
        }
    }

    // Método para eliminar un Cliente
    public void delete(Cliente e) throws Exception {
        String sql = "delete " +
                "from Cliente " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Cliente NO EXISTE");
        }
    }

    // Método para buscar Clientes que coincidan con un criterio (por ejemplo, nombre)
    public List<Cliente> search(Cliente e) throws Exception {
        List<Cliente> resultado = new ArrayList<Cliente>();
        String sql = "select * " +
                "from " +
                "Cliente t " +
                "where t.id like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            Cliente r = from(rs, "t");
            resultado.add(r);
        }
        return resultado;
    }

    // Método para mapear el ResultSet a un objeto Cliente
    public Cliente from(ResultSet rs, String alias) throws Exception {
        Cliente e = new Cliente();
        e.setId(rs.getString(alias + ".id"));
        e.setNombre(rs.getString(alias + ".nombre"));
        e.setTelefono(rs.getString(alias + ".telefono"));
        e.setEmail(rs.getString(alias + ".email"));
        e.setDescuento(rs.getInt(alias + ".descuento"));
        return e;
    }
}
