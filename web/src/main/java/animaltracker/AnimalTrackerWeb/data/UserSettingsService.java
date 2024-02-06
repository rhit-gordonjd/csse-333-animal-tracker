package animaltracker.AnimalTrackerWeb.data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserSettingsService {
    @Autowired
    private DataSource dataSource;

    public boolean updateDisplayName(int userID, String newDisplayName) {
        try {
            Connection connection = dataSource.getConnection();
            CallableStatement stmt = connection.prepareCall("{? = call UpdateDisplayName(@UserID = ?, @NewDisplayName = ?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, userID);
            stmt.setString(3, newDisplayName);

            stmt.executeUpdate();

            int returnValue = stmt.getInt(1);

            if (returnValue != 0) {
                throw new SQLException("Change display name was not successful" + returnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateUsername(int userID, String newUsername) {
        try {
            Connection connection = dataSource.getConnection();
            CallableStatement stmt = connection.prepareCall("{? = call UpdateUsername(@UserID = ?, @NewUsername = ?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, userID);
            stmt.setString(3, newUsername);

            stmt.executeUpdate();

            int returnValue = stmt.getInt(1);

            if (returnValue != 0) {
                throw new SQLException("Change Username was not Sucessful" + returnValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updatePassword(int userID, String newPass) {
        try {
            Connection connection = dataSource.getConnection();
            CallableStatement stmt = connection.prepareCall("{? = call ChangePassword(@UserID = ?, @NewPasswordHash = ?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, userID);
            stmt.setString(3, newPass);

            stmt.executeUpdate();

            int returnValue = stmt.getInt(1);
            if (returnValue != 0) {
                throw new SQLException("Change Password was not Sucessful" + returnValue);
            }
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true;
    }
}
