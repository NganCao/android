package com.th.thuhien.plantshop.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;
import com.th.thuhien.plantshop.R;
import com.th.thuhien.plantshop.adapter.MenuAdapter;
import com.th.thuhien.plantshop.adapter.SanPhamAdapter;
import com.th.thuhien.plantshop.model.GioHang;
import com.th.thuhien.plantshop.model.Menu;
import com.th.thuhien.plantshop.model.SanPham;
import com.th.thuhien.plantshop.ultil.MenuService;
import com.th.thuhien.plantshop.ultil.SanPhamService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    //ListView lv_SanPhamMoi;
    RecyclerView recyclerViewmanhinhchinh;
    NavigationView navigationView;
    ListView listViewManhinhchinh;
    DrawerLayout drawerLayout;

    ArrayList<Menu> arrayListMenu = new ArrayList<Menu>();
    ArrayList<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;

    MenuAdapter menuAdapter;

    //String tenMenu = "";


    public static ArrayList<GioHang> arrayGioHang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AnhXa();
        ActionBar();
        ActionViewFlipper();
        ClickItemMenu();
        //LoadMoreData(); // đưa về màn hình chi tiết -> chưa làm được
    }



    private void ClickItemMenu() {
        listViewManhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    Intent intent = new Intent(MainActivity.this, ManHinhSanPhamActivity.class);
                    intent.putExtra("maMenu", arrayListMenu.get(position).getMaMenu());
                    intent.putExtra("tenmenu", arrayListMenu.get(position).getTenMenu());
                    startActivity(intent);
                }
            }
        });
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://1.bp.blogspot.com/-xA1DQYrcADo/XJcgoCV5NpI/AAAAAAAAF3k/f8UlgDDuYtMZzF3kF7-1JY7ITExYSgLzgCLcBGAs/s1600/banner-23.png");
        mangquangcao.add("https://4.bp.blogspot.com/-oBLvbX7wo1I/XJcgofGR5iI/AAAAAAAAF3o/Mih1AppGVrkz55R59Nis9KaAm5BZoEHWQCLcBGAs/s1600/banner-the-gioi-cay-va-hoa-961x343.png");
        mangquangcao.add("https://2.bp.blogspot.com/-sGgiHw_oGVo/XJcgoOP-VEI/AAAAAAAAF3g/nzkCfma8K_8QyRzTbYiW5IMaWYjBJcMpwCLcBGAs/s1600/banner_collection_master.jpg");
        mangquangcao.add("https://1.bp.blogspot.com/-6VB-i_N81fc/XJcgooEER7I/AAAAAAAAF3s/qb7a1FGtvysv2yXpnWlgwBNSgYB4tCL5wCLcBGAs/s1600/shop-cay-canh-tai-da-nang-10.jpg");

        for (int i = 0; i < mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);

        Animation animation_slide_in_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in_right);
        viewFlipper.setInAnimation(animation_slide_out_right);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewmanhinhchinh = findViewById(R.id.recyclerview);
        //lv_SanPhamMoi = findViewById(R.id.listViewSanPhamMoi);
        navigationView = findViewById(R.id.navigationView);
        listViewManhinhchinh = findViewById(R.id.listViewManhinhchinh);
        drawerLayout = findViewById(R.id.drawerLayout);

        //arrayListMenu.add(0, new Menu(0, "Trang chá»§"));
        menuAdapter = new MenuAdapter(arrayListMenu, getApplicationContext());
        listViewManhinhchinh.setAdapter(menuAdapter);

        arrayListMenu.add(new Menu(0, "Trang chủ"));

        AsynListMenu asynListMenu = new AsynListMenu();
        asynListMenu.execute();

        mangSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(),mangSanPham);
        recyclerViewmanhinhchinh.setHasFixedSize(true);
        recyclerViewmanhinhchinh.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerViewmanhinhchinh.setAdapter(sanPhamAdapter);

        AsysListSanPhamMoi asysListSanPhamMoi = new AsysListSanPhamMoi();
        asysListSanPhamMoi.execute(6);

        if (arrayGioHang != null){


        }else {
            arrayGioHang = new ArrayList<>();
        }
    }


    public class AsynListMenu extends AsyncTask<Void, Void, List<Menu>>{


        @Override
        protected List<Menu> doInBackground(Void... voids) {
            MenuService menuService = new MenuService();
            return menuService.getListMenu();
        }

        @Override
        protected void onPostExecute(List<Menu> menu) {
            super.onPostExecute(menu);
            for (int i = 0; i < menu.size(); i++){
                arrayListMenu.add(menu.get(i));
            }
            menuAdapter.notifyDataSetChanged();
        }
    }

    public class AsysListSanPhamMoi extends AsyncTask<Integer, Void, List<SanPham>>{

        @Override
        protected List<SanPham> doInBackground(Integer... integers) {
            SanPhamService sanPhamService = new SanPhamService();
            return sanPhamService.getListSpMoi(integers[0]);
        }

        @Override
        protected void onPostExecute(List<SanPham> sanPhams) {
            super.onPostExecute(sanPhams);
            for (int i = 0; i < sanPhams.size(); i++){
                mangSanPham.add(sanPhams.get(i));
            }
            sanPhamAdapter.notifyDataSetChanged();
        }
    }

}
