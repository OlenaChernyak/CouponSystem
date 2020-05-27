package common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class ResourseUtils {
	private ResourseUtils() {
		throw new AssertionError("Non instantiable");
	}

	public static void close(Statement... statements) throws SystemMalfunctionException {
		try {
			for (int i = 0; i < statements.length; i++) {
				if (statements[i] != null) {
					statements[i].close();
				}
			}
		} catch (SQLException e) {
			throw new SystemMalfunctionException("Unable to close the statement" + e.getMessage());
		}

	}
	
	public static void close(ResultSet... resultSets) throws SystemMalfunctionException {
		try {
			for (int i = 0; i < resultSets.length; i++) {
				if (resultSets[i] != null) {
					resultSets[i].close();
				}
			}
		} catch (SQLException e) {
			throw new SystemMalfunctionException("Unable to close the statement" + e.getMessage());
		}

	}

}
