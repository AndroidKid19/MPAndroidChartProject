package com.wheel.chart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wheel.chart.activity.BarChartActivity;
import com.wheel.chart.activity.CombinedChartActivity;
import com.wheel.chart.activity.DynamicLineChartActivity;
import com.wheel.chart.activity.LineChartActivity;
import com.wheel.chart.activity.MeterActivity;
import com.wheel.chart.activity.PieChartActivity;
import com.wheel.chart.activity.RadarChartActivity;

/**
 * ================================================
 * Description: MPAndroidChart v3.1.0
 * <p>
 * Created by MPAndroidChartProject on 10/10/2019 14:12
 * <a href="mailto:yuanw9@sina.com">Contact me</a>
 * ================================================
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onCardViewClick(View v) {
        switch (v.getId()) {
            case R.id.view_line_chart:
                startActivity(new Intent(this, LineChartActivity.class));
                break;
            case R.id.view_line_chart_date:
                startActivity(new Intent(this, DynamicLineChartActivity.class));
                break;
            case R.id.view_bar_chart:
                startActivity(new Intent(this, BarChartActivity.class));
                break;
            case R.id.view_pie_chart:
                startActivity(new Intent(this, PieChartActivity.class));
                break;
            case R.id.view_meter:
                startActivity(new Intent(this, MeterActivity.class));
                break;
            case R.id.view_radar:
                startActivity(new Intent(this, RadarChartActivity.class));
                break;
            case R.id.view_combined:
                startActivity(new Intent(this, CombinedChartActivity.class));
                break;
        }
    }
}
