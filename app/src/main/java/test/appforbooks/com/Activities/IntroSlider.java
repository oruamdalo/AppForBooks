package test.appforbooks.com.Activities;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import test.appforbooks.com.Adapters.ClassroomAdapter;
import test.appforbooks.com.MainActivity;
import test.appforbooks.com.R;
import test.appforbooks.com.SchoolUtils.ClassRoom;
import test.appforbooks.com.SchoolUtils.DataGetter;
import test.appforbooks.com.SchoolUtils.ResultManager;
import test.appforbooks.com.SchoolUtils.School;
import test.appforbooks.com.SchoolUtils.SchoolFinder;
import test.appforbooks.com.SchoolUtils.SchoolManager;
import test.appforbooks.com.SchoolUtils.SchoolManagerInterface;
import test.appforbooks.com.SchoolUtils.SchoolPicker;
import test.appforbooks.com.Utils.CustomViewPager;
import test.appforbooks.com.Utils.PrefManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class IntroSlider extends AppCompatActivity{

    private CustomViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;

    private boolean canScroll;
    private int currentPagePosition;

    //Data to pass to MainActivity once finished
    ArrayList<String> regionList, municList, provinceList, provinceKeys, schoolList;
    ArrayList<School> fullSchoolList;
    ArrayList<ClassRoom> classroomList;
    SpinnerDialog regionSpinner, provinceSpinner, municSpinner, schoolSpinner;
    String regionCode, municCode, provinceCode;
    String schoolGrade, schoolCode = null;
    SchoolManager schoolManager;
    SchoolPicker schoolPicker;
    ClassroomAdapter classroomAdapter;
    int schoolPosition;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPagePosition = 0;
        canScroll = false;

        regionList = new ArrayList<>();
        municList = new ArrayList<>();
        provinceList = new ArrayList<>();
        provinceKeys = new ArrayList<>();
        schoolList = new ArrayList<>();
        classroomList = new ArrayList<>();
        initList();

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_intro_slider);

        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.intro_slide1,
                R.layout.intro_slide2,
                R.layout.intro_slide3,
                R .layout.intro_slide4};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(-1);
                if (current >= 0 && canScroll) {
                    // move to next screen
                    //uncomment to enable the back button
                    //viewPager.setCurrentItem(current);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length && checkIfCanScroll(currentPagePosition)) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else if(current == layouts.length - 1) {
                    launchHomeScreen();
                }
            }
        });

        //Deactive the page scrolling
        viewPager.setPagingEnabled(false);

        //Not used because of logistic issues
        /*viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("TOUCH","SCREEN TOUCHED");
                //TODO: remove the comment and the true statement
                canScroll = true;//checkIfCanScroll(currentPagePosition);
                viewPager.setPagingEnabled(canScroll);
                return false;
            }

        });*/

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroSlider.this, MainActivity.class));
        finish();
    }

    private boolean checkIfCanScroll(int position){
        switch (position){
            case 0:
                String firstName = ((EditText)findViewById(R.id.firstname_input)).getText().toString();
                String lastName = ((EditText)findViewById(R.id.lastname_input)).getText().toString();
                return (!firstName.isEmpty() && !lastName.isEmpty());
            case 1:
                return schoolManager != null;
            default:
                    break;
        }
        return true;
    }



    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            currentPagePosition = position;
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.confirmBtn));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.nextBtnText));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
//            Toast.makeText(IntroSlider.this, "Sliding: "+(canScroll = checkIfCanScroll(currentPagePosition)), Toast.LENGTH_SHORT).show();
//            canScroll = checkIfCanScroll(currentPagePosition);
//            viewPager.setPagingEnabled(canScroll);
        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            switch (position){
                case 0:
                    String[] myResArray = getResources().getStringArray(R.array.school_grades);
                    Spinner slideOneSpinner = setupSpinner(R.id.schoolgrade_spinner, new ArrayList(Arrays.asList(myResArray)));
                    slideOneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            schoolGrade = Integer.toString(position);
                            Toast.makeText(IntroSlider.this, schoolGrade, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    break;
                case 1:
                    initSlideTwo();
                    break;
                case 2:
                    initSlideThree();
                    break;
                case 3:
                    break;
                default:
                    break;

            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private Spinner setupSpinner(int viewId, ArrayList<String> data){

        Spinner spinner = findViewById(viewId);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_layout, data);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerArrayAdapter);
        return spinner;
    }

    private void initSlideTwo(){
        final SpinnerDialog regionSpinner = new SpinnerDialog(IntroSlider.this, regionList, "Seleziona la regione");
//        municSpinner = new SpinnerDialog(CityChooser.this, municList, "Seleziona il comune");


        //Spinner dialog listeners
        regionSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                provinceList.clear();
                municList.clear();
                schoolList.clear();
                ((Button)findViewById(R.id.city_button)).setText(getString(R.string.hintCity));
                ((Button)findViewById(R.id.province_button)).setText(getString(R.string.hintProvince));
                ((Button)findViewById(R.id.school_button)).setText(getString(R.string.hintSchool));


                String pos = Integer.toString(position+1);
                String res = (position<9)?"0"+pos:pos;
                regionCode = res;
                schoolPicker = new SchoolPicker(res);
                ((Button)findViewById(R.id.region_button)).setText(item);

                schoolPicker.getProvinces(IntroSlider.this, new SchoolManagerInterface() {
                    @Override
                    public void getResults(ArrayList result) {

                    }

                    @Override
                    public void getResults(HashMap result) {
                        HashMap<String, String> res = new HashMap<>(result);
                        for(String key : res.keySet()){
                            provinceList.add(res.get(key));
                            provinceKeys.add(key);
                        }
                        initProvince();
                    }
                });



            }
        });


        //Button click listeners
        findViewById(R.id.region_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regionSpinner.showSpinerDialog();
            }
        });
    }


    private void initProvince(){
        final SpinnerDialog provinceSpinner = new SpinnerDialog(IntroSlider.this, provinceList, "Seleziona la provincia");
        provinceSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                ((Button)findViewById(R.id.city_button)).setText(item);


                schoolPicker.getMunicipality(provinceKeys.get(position), IntroSlider.this, new SchoolManagerInterface() {
                    @Override
                    public void getResults(ArrayList result) {

                    }

                    @Override
                    public void getResults(HashMap result) {
                        HashMap<String, String> res = new HashMap<>(result);
                        for(String key : res.keySet()){
                            municList.add(res.get(key));
                        }
                        initMunic();
                    }
                });
            }
        });
        findViewById(R.id.city_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provinceSpinner.showSpinerDialog();
            }
        });
    }

    private void initMunic(){
        schoolList.clear();
        municSpinner = new SpinnerDialog(IntroSlider.this, municList, "Seleziona il comune");
        municSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                ((Button)findViewById(R.id.province_button)).setText(item);

                schoolPicker.getSchool(item, schoolGrade, IntroSlider.this, new SchoolManagerInterface() {
                    @Override
                    public void getResults(ArrayList result) {
                        //TODO: put in schoolList the res values
                        ArrayList<School> res = new ArrayList<School>(result);
                        fullSchoolList = new ArrayList<School>(result);
                        for(int i=0;i<res.size();i++){
                            String schoolName = res.get(i).getName()
                                    + " " + res.get(i).getType();
                            schoolList.add(schoolName);
                        }
                        initSchool();
                    }

                    @Override
                    public void getResults(HashMap result) {

                    }
                });
            }
        });
        findViewById(R.id.province_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                municSpinner.showSpinerDialog();
            }
        });
    }

    private void initSchool(){
        schoolSpinner = new SpinnerDialog(IntroSlider.this, schoolList, "Seleziona la scuola");
        schoolSpinner.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                ((Button)findViewById(R.id.school_button)).setText(item);

                //TODO: make this work correctly
//                Intent i = getIntent();
//                Bundle extras = i.getExtras();
//                String data = extras.getString("schoolGrade");
//                Log.d("SchoolGrade: ",Integer.toString(data.length()));
//                Toast.makeText(CityChooser.this,"SchoolGrade: "+data,Toast.LENGTH_SHORT).show();
                Toast.makeText(IntroSlider.this, fullSchoolList.get(position).getCode(), Toast.LENGTH_SHORT).show();
                schoolManager = new SchoolManager(fullSchoolList.get(position).getCode(), IntroSlider.this);
                schoolCode = fullSchoolList.get(position).getCode();
                schoolManager.getClasses(IntroSlider.this, new SchoolManagerInterface() {
                    @Override
                    public void getResults(ArrayList result) {
                        classroomList = new ArrayList<>(result);
                        Log.d("CLASSI: ",result.toString());
                        GridView gridView = (GridView)findViewById(R.id.classrooms_gridview);
                        classroomAdapter = new ClassroomAdapter(IntroSlider.this, classroomList);
                        gridView.setAdapter(classroomAdapter);
                    }

                    @Override
                    public void getResults(HashMap result) {

                    }
                });

            }
        });
        findViewById(R.id.school_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schoolSpinner.showSpinerDialog();
            }
        });
    }

    private void initSlideThree(){
        //SLIDE 3 GRIDVIEW ADAPTER

    }

    private void initList(){
        String provinces[] = new String[]{
                "Abruzzo","Valle D Aosta","Basilicata","Calabria", "Campania",
                "Emilia Romagna","Friuli Venezia Giulia","Lazio","Liguria","Lombardia",
                "Marche","Molise","Piemonte","Puglia","Sardegna",
                "Sicilia","Toscana","Trentino Alto Adige","Umbria","Veneto"
        };
        for(String item : provinces){
            regionList.add(item);
        }


    }
}
