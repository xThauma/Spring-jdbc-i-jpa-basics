package appJDBC;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("user")
public class RepositoryFacadePS4Impl implements RepositoryFacadePS4 {

	private JdbcTemplate jdbcTemplate;

	public RepositoryFacadePS4Impl(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	@Transactional
	public void save(User c) {
		final String SQL = "insert into CUSTOMER(USERNAME,PASSWORD,NAME,EMAIL) values (?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL, new String[] { "ID" });
				ps.setString(1, c.getUsername());
				ps.setString(2, c.getPassword().toString());
				ps.setString(3, c.getName());
				ps.setString(4, c.getEmail());
				return ps;
			}
		}, keyHolder);

		c.setId(keyHolder.getKey().longValue());
	}

	@Transactional
	@Override
	public void update(User c) {
		final String SQL = "update CUSTOMER set USERNAME=?, PASSWORD=?, NAME=?, EMAIL=? where ID=?";
		jdbcTemplate.update(SQL, c.getUsername(), c.getPassword().toString(), c.getName(), c.getEmail(), c.getId());
	}

	@Transactional
	@Override
	public void delete(Long id) {
		final String SQL = "delete from CUSTOMER where ID=?";
		int n = jdbcTemplate.update(SQL, id);
	}

	@Override
	public int count() {
		final String SQL = "select count(*) from CUSTOMER";
		return jdbcTemplate.queryForObject(SQL, Integer.class);
	}

	@Override
	public List<User> findAll() {
		final String SQL = "select * from CUSTOMER";
		return jdbcTemplate.query(SQL, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User c = new User();
				c.setId(rs.getLong("ID"));
				c.setUsername(rs.getString("USERNAME"));
				c.setPassword((getHash(rs.getString("PASSWORD").toCharArray())));
				c.setName(rs.getString("NAME"));
				c.setEmail(rs.getString("EMAIL"));
				return c;
			}
		});
	}

	@Override
	public char[] getHash(char[] s) {
		String password = "";
		for (int i = 0; i < s.length; i++) {
			password += s[i];
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash;
			hash = digest.digest(password.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			password = hexString.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return password.toCharArray();
	}

}
