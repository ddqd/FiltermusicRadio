package filtermusic.net.common.data;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import filtermusic.net.common.database.DatabaseHelper;
import filtermusic.net.common.database.DbRadio;
import filtermusic.net.common.model.Radio;

/**
 * Provides data from the database
 */
/*package*/ class DatabaseDataProvider {

    private Context mContext;

    DatabaseDataProvider(Context context) {
        this.mContext = context;
    }

    public void provideRadioList(DataProvider.RadioListRetrievedListener listener) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        List<DbRadio> dbRadios = dao.queryForAll();
        List<Radio> radios = convert(dbRadios);
        listener.onRadioListRetrieved(radios);
    }

    private List<Radio> convert(final List<DbRadio> dbRadios) {
        List<Radio> radios = new ArrayList<Radio>();
        for (DbRadio dbRadio : dbRadios) {
            final Radio radio = new Radio(
                    dbRadio.getTitle(), dbRadio.getURL(), dbRadio.getGenre(),
                    dbRadio.getDescription(), dbRadio.getCategory(), dbRadio.getImageUrl(),
                    dbRadio.isFavorite(), dbRadio.getPlayedDate());
            radios.add(radio);
        }

        return radios;
    }

    /**
     * Retrieve from the database the fields marked as favorite
     * @param listener notifies the requestor object that the list of favorites was retrieved
     */
    public void provideFavoritesList(DataProvider.FavoritesRetrievedListener listener) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        List<DbRadio> dbRadios = dao.queryForEq(DbRadio.IS_FAVORITE_FILED_NAME, true);
        List<Radio> radios = convert(dbRadios);
        listener.onFavoritesRetrieved(radios);
    }

    /**
     * Retrieve from the database all the fields ordered by their play date
     * @param listener notifies the requestor object that the list of last played was retrieved
     */
    public void provideLastPlayedList(DataProvider.LastPlayedRetrievedListener listener) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        RuntimeExceptionDao<DbRadio, Integer> dao = databaseHelper.getDbRadioDao();

        QueryBuilder<DbRadio, Integer> queryBuilder = dao.queryBuilder();
        queryBuilder.orderBy(DbRadio.PLAYED_DATE_FILED_NAME, false);

        List<DbRadio> dbRadios = null;
        try {
            dbRadios = dao.query(queryBuilder.prepare());
            List<Radio> radios = convert(dbRadios);
            listener.onLastPlayedRetrieved(radios);
        } catch (SQLException e) {
            e.printStackTrace();

            listener.onLastPlayedRetrieved(new ArrayList<Radio>());
        }
    }

    public void updateRadio(Radio radio) {

    }
}