package org.kpa.risk.modeling;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.DefaultXYDataset;

import java.awt.*;
import java.util.LinkedList;

public class Statistics {
    private final DescriptiveStatistics plStat = new DescriptiveStatistics();
    private final DescriptiveStatistics ddStat = new DescriptiveStatistics();

    @Override
    public String toString() {
        return "Statistics{" +
                "pl(mean/sigma)=" + plStat.getMean() + "/" + plStat.getStandardDeviation() +
                ", dd(99%)=" + ddStat.getPercentile(99.9) +
                '}';
    }

    public void addResult(double pl, double maxDd) {
        Preconditions.checkArgument(maxDd >= 0);
        ddStat.addValue(maxDd);
        plStat.addValue(pl);
    }

    private void populate(DefaultXYDataset ds) {
        double[] dd = ddStat.getValues();
        double[] pl = plStat.getValues();
        double[][] series = new double[2][dd.length];
        for (int i = 0; i < dd.length; i++) {
            series[0][i] = pl[i];
            series[1][i] = dd[i];
        }
        ds.addSeries("pl/dd", series);
    }


    public void showChart() {
        DefaultXYDataset dataSet = new DefaultXYDataset();
        populate(dataSet);
        JFreeChart jfreechart = ChartFactory.createScatterPlot("Monte Carlo test for RiskModel", "pl(cumulative)", "dd(max)", dataSet, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        xyplot.setRangeTickBandPaint(new Color(200, 200, 100, 100));
        XYDotRenderer xydotrenderer = new XYDotRenderer();
        xydotrenderer.setDotWidth(4);
        xydotrenderer.setDotHeight(4);
        xyplot.setRenderer(xydotrenderer);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        xyplot.getRangeAxis().setInverted(false);
        ChartFrame frame = new ChartFrame("Test", jfreechart);
        frame.pack();
        frame.setVisible(true);
    }

    public java.util.List<String> dumpString() {
        double[] dd = ddStat.getValues();
        double[] pl = plStat.getValues();
        java.util.List<String> dump = new LinkedList<>();
        dump.add("#\tpl(x)\tdd(y)\tpl/dd");
        for (int i = 0; i < dd.length; i++) {
            if (dd[i] > 0) {
                dump.add(String.format("%1s\t%2s\t%3s\t%4s", i, pl[i], dd[i], pl[i] / dd[i]));
            } else {
                dump.add(String.format("%1s\t%2s\t%3s\t%4s", i, pl[i], dd[i], "n/a"));
            }
        }
        return dump;
    }
}