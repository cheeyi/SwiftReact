// Charting functionality by Fall '14 team. Not in use 5/2/2015
/*package com.design.senior.realmaster;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

*
 * Created by irfgag on 10/29/2014.

public class ChartMaker {

    public String getName() {
        return "Reaction Time";
    }

    public String getDesc() {
        return "(line chart)";
    }

    public long findmin(long[] array){
        long currentmin = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i] < currentmin){
                currentmin = array[i];
            }
        }
        return currentmin;
    }

    public long findmax(long[] array){
        long currentmax = 0;
        int location = 0;
        for(int i = 0; i < array.length; i++){
            if(array[i] > currentmax){
                currentmax = array[i];
                location = i;
            }
        }
        return currentmax;
    }

    public Intent execute(Context context) {

        String[] titles = {"Average Reaction Time"};
        List<long[]> reactionTimes = new ArrayList<long[]>();
        reactionTimes.add(DefaultActivity.data.getAllAverageReactionTimes());
        int index = reactionTimes.get(0).length;
        List<long[]> x = new ArrayList<long[]>();
        x.add(new long[index]);

        for (int i = 0; i < index; i++){
            x.get(0)[i] = i+1;
        }

        int[] colors = new int[] {Color.GREEN};
        PointStyle[] styles = new PointStyle[] {PointStyle.CIRCLE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

        int length = renderer.getSeriesRendererCount();
        for(int i = 0; i < length; i++){
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        long xmin, xmax, ymin, ymax;
        int xcheck;
        if(index < 10){
            xmin = 0;
            xmax = index;
            xcheck = index;
        }
        else{
            xmin = index - 10;
            xmax = index;
            xcheck = 10;
        }
        ymin = findmin(reactionTimes.get(0));
        ymax = findmax(reactionTimes.get(0));

        setChartSettings(renderer, "Reaction Time Chart", "Session Number", "Reaction Time (ms)", xmin , xmax, ymin, ymax, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(xcheck);
        renderer.setYLabels(5);
        renderer.setShowGrid(true);
        renderer.setYLabelsAlign(Paint.Align.LEFT);
        renderer.setXLabelsAlign(Paint.Align.RIGHT);
        renderer.setZoomButtonsVisible(false);
        //renderer.setPanLimits(new double[] {0, 20, 50, 350});
        //renderer.setZoomLimits(new double[] {0, 20, 50, 350});

        XYMultipleSeriesDataset dataset = buildDataset(titles, x, reactionTimes);
        XYSeries series = dataset.getSeriesAt(0);
        Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer, "Reaction Time");
        return intent;

    }

    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<long[]> xValues,
                                                   List<long[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[] { 20, 30, 15, 20 });
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<long[]> xValues,
                            List<long[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            long[] xV = xValues.get(i);
            long[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }
}
*/