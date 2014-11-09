package filtermusic.net.categories;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import filtermusic.net.R;
import filtermusic.net.common.model.Category;
import filtermusic.net.common.model.Radio;

/**
 * Holds a view flipper where the user can browse between categories and radios corresponding to categories
 */
public class CategoriesFragment extends Fragment implements CategoriesController.DataListener {

    private static final String LOG_TAG = CategoriesFragment.class.getSimpleName();

    private static final int PROGRESS_VIEW_INDEX = 0;
    private static final int CATEGORIES_VIEW_INDEX = 1;
    private static final int RADIOS_VIEW_INDEX = 2;
    private static final int RADIO_DETAIL_VIEW_INDEX = 3;

    private ListView mCategoriesList;
    private ListView mRadiosList;
    private RadioDetailView mRadioDetailView;

    private ViewFlipper mViewFlipper;

    private CategoriesController mCategoriesController;

    private List<Category> mCategories = new ArrayList<Category>();

    private Category mLastOpenedCategory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);

        setupUI(rootView);

        mCategoriesController = CategoriesController.getInstance();
        mCategoriesController.setDataListener(this);

        if (!mCategoriesController.getCategories().isEmpty()) {
            onCategoriesUpdated(mCategoriesController.getCategories());
        }
        return rootView;
    }

    private void setupUI(final View rootView) {
        mViewFlipper = (ViewFlipper) rootView.findViewById(R.id.view_flipper);
        mCategoriesList = (ListView) rootView.findViewById(R.id.categories_list);
        mRadiosList = (ListView) rootView.findViewById(R.id.radios_list);
        mRadioDetailView = (RadioDetailView) rootView.findViewById(R.id.radio_detail_view);

        mCategoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flipToPage(RADIOS_VIEW_INDEX);

                mLastOpenedCategory = mCategories.get(position);
                RadiosAdapter radiosAdapter = new RadiosAdapter(getActivity(), R.layout.radio_list_item, mLastOpenedCategory.getRadioList());
                mRadiosList.setAdapter(radiosAdapter);
            }
        });

        mRadiosList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open the radio details
                Radio radio = mLastOpenedCategory.getRadioList().get(position);
                mRadioDetailView.setRadio(radio);
                flipToPage(RADIO_DETAIL_VIEW_INDEX);
            }
        });
    }

    @Override
    public void onCategoriesUpdated(List<Category> categories) {

        updateCategories(categories);
        if (PROGRESS_VIEW_INDEX == mViewFlipper.getDisplayedChild()) {
            flipToPage(CATEGORIES_VIEW_INDEX);
        }
    }

    private void updateCategories(List<Category> categories) {
        mCategories = categories;
        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(getActivity(), R.layout.category_item, mCategories);
        mCategoriesList.setAdapter(categoriesAdapter);
    }

    private void flipToPage(int pageIndex) {
        mViewFlipper.setDisplayedChild(pageIndex);
    }

    public void onBackPressed(){
        int currentPage = mViewFlipper.getDisplayedChild();
        switch (currentPage){
            case RADIOS_VIEW_INDEX:
                flipToPage(CATEGORIES_VIEW_INDEX);
                break;
            case RADIO_DETAIL_VIEW_INDEX:
                flipToPage(RADIOS_VIEW_INDEX);
                break;
            case PROGRESS_VIEW_INDEX:
            case CATEGORIES_VIEW_INDEX:
                getActivity().finish();
        }
    }

}
