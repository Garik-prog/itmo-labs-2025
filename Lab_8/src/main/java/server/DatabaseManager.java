package server;

import common.models.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String host, String dbName, String user, String password) throws SQLException {
        String url = "jdbc:postgresql://" + host + "/" + dbName;
        this.connection = DriverManager.getConnection(url, user, password);
        initSchema();
    }

    private void initSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SEQUENCE IF NOT EXISTS flat_id_seq");
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "    login VARCHAR(255) PRIMARY KEY," +
                            "    password_hash VARCHAR(32) NOT NULL" +
                            ")"
            );
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS flats (" +
                            "    id INTEGER PRIMARY KEY DEFAULT nextval('flat_id_seq')," +
                            "    map_key VARCHAR(255) NOT NULL," +
                            "    name VARCHAR(255) NOT NULL," +
                            "    coord_x INTEGER NOT NULL," +
                            "    coord_y BIGINT NOT NULL," +
                            "    creation_date TIMESTAMP NOT NULL," +
                            "    area BIGINT NOT NULL," +
                            "    number_of_rooms INTEGER NOT NULL," +
                            "    furnish VARCHAR(50)," +
                            "    view VARCHAR(50)," +
                            "    transport VARCHAR(50)," +
                            "    house_name VARCHAR(255)," +
                            "    house_year INTEGER," +
                            "    house_flats_on_floor INTEGER," +
                            "    owner_login VARCHAR(255) NOT NULL REFERENCES users(login)" +
                            ")"
            );
        }
    }

    public boolean registerUser(String login, String passwordHash) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO users(login, password_hash) VALUES(?, ?)")) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean authenticateUser(String login, String passwordHash) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT 1 FROM users WHERE login=? AND password_hash=?")) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public int insertFlat(String key, Flat flat, String ownerLogin) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO flats(map_key, name, coord_x, coord_y, creation_date, area, " +
                        "number_of_rooms, furnish, view, transport, house_name, house_year, " +
                        "house_flats_on_floor, owner_login) " +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id")) {
            ps.setString(1, key);
            ps.setString(2, flat.getName());
            ps.setInt(3, flat.getCoordinates().getX());
            ps.setLong(4, flat.getCoordinates().getY());
            ps.setTimestamp(5, new Timestamp(flat.getCreationDate().getTime()));
            ps.setLong(6, flat.getArea());
            ps.setInt(7, flat.getNumberOfRooms());
            ps.setString(8, flat.getFurnish() != null ? flat.getFurnish().name() : null);
            ps.setString(9, flat.getView() != null ? flat.getView().name() : null);
            ps.setString(10, flat.getTransport() != null ? flat.getTransport().name() : null);
            if (flat.getHouse() != null) {
                ps.setString(11, flat.getHouse().getName());
                ps.setInt(12, flat.getHouse().getYear());
                ps.setObject(13, flat.getHouse().getNumberOfFlatsOnFloor());
            } else {
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.INTEGER);
                ps.setNull(13, Types.INTEGER);
            }
            ps.setString(14, ownerLogin);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    public boolean updateFlat(int id, Flat flat, String requesterLogin) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE flats SET name=?, coord_x=?, coord_y=?, area=?, number_of_rooms=?, " +
                        "furnish=?, view=?, transport=?, house_name=?, house_year=?, house_flats_on_floor=? " +
                        "WHERE id=? AND owner_login=?")) {
            ps.setString(1, flat.getName());
            ps.setInt(2, flat.getCoordinates().getX());
            ps.setLong(3, flat.getCoordinates().getY());
            ps.setLong(4, flat.getArea());
            ps.setInt(5, flat.getNumberOfRooms());
            ps.setString(6, flat.getFurnish() != null ? flat.getFurnish().name() : null);
            ps.setString(7, flat.getView() != null ? flat.getView().name() : null);
            ps.setString(8, flat.getTransport() != null ? flat.getTransport().name() : null);
            if (flat.getHouse() != null) {
                ps.setString(9, flat.getHouse().getName());
                ps.setInt(10, flat.getHouse().getYear());
                ps.setObject(11, flat.getHouse().getNumberOfFlatsOnFloor());
            } else {
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.INTEGER);
                ps.setNull(11, Types.INTEGER);
            }
            ps.setInt(12, id);
            ps.setString(13, requesterLogin);
            return ps.executeUpdate() > 0;
        }
    }

    public void deleteFlatByKey(String key, String requesterLogin) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM flats WHERE map_key=? AND owner_login=?")) {
            ps.setString(1, key);
            ps.setString(2, requesterLogin);
            ps.executeUpdate();
        }
    }

    public int clearUserFlats(String requesterLogin) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM flats WHERE owner_login=?")) {
            ps.setString(1, requesterLogin);
            return ps.executeUpdate();
        }
    }


    public List<Integer> deleteLowerFlats(Flat flat, String requesterLogin) throws SQLException {
        List<Integer> deleted = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM flats WHERE owner_login=? AND " +
                        "(area < ? OR (area = ? AND number_of_rooms < ?) OR " +
                        " (area = ? AND number_of_rooms = ? AND name < ?)) RETURNING id")) {
            long area = flat.getArea();
            int rooms = flat.getNumberOfRooms();
            String name = flat.getName();
            ps.setString(1, requesterLogin);
            ps.setLong(2, area);
            ps.setLong(3, area);
            ps.setInt(4, rooms);
            ps.setLong(5, area);
            ps.setInt(6, rooms);
            ps.setString(7, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) deleted.add(rs.getInt(1));
        }
        return deleted;
    }

    public LinkedHashMap<String, Flat> loadAllFlats() throws SQLException {
        LinkedHashMap<String, Flat> map = new LinkedHashMap<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM flats ORDER BY id")) {
            while (rs.next()) {
                map.put(rs.getString("map_key"), resultSetToFlat(rs));
            }
        }
        return map;
    }

    private Flat resultSetToFlat(ResultSet rs) throws SQLException {
        Flat flat = new Flat();

        flat.setId(rs.getInt("id"));
        flat.setName(rs.getString("name"));

        Coordinates coords = new Coordinates();
        coords.setX(rs.getInt("coord_x"));
        coords.setY(rs.getLong("coord_y"));
        flat.setCoordinates(coords);

        flat.setCreationDate(rs.getTimestamp("creation_date"));
        flat.setArea(rs.getLong("area"));
        flat.setNumberOfRooms(rs.getInt("number_of_rooms"));

        String furnish = rs.getString("furnish");
        if (furnish != null) flat.setFurnish(Furnish.valueOf(furnish));

        String view = rs.getString("view");
        if (view != null) flat.setView(View.valueOf(view));

        String transport = rs.getString("transport");
        if (transport != null) flat.setTransport(Transport.valueOf(transport));

        String houseName = rs.getString("house_name");
        if (houseName != null) {
            House house = new House();
            house.setName(houseName);
            house.setYear(rs.getInt("house_year"));
            int flatsOnFloor = rs.getInt("house_flats_on_floor");
            if (!rs.wasNull()) house.setNumberOfFlatsOnFloor(flatsOnFloor);
            flat.setHouse(house);
        }

        flat.setOwnerLogin(rs.getString("owner_login"));
        return flat;
    }
}
