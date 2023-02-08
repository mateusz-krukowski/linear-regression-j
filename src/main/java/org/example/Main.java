package org.example;

import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

import static org.math.plot.utils.Array.max;
import static org.math.plot.utils.Array.min;

/** @author Mateusz Stanisław Krukowski **/
/*Linear Regression counting program */
public class Main {
    private static final int n = 128; //length
    public static double[] x = new double[n];
    public static double[] y = new double[n];
    public static double x̄ = 0; //mean value of x
    public static double ȳ = 0; // mean value of y
    public static double a = 0; // slope / współczynnik kierunkowy
    public static double b = 0; // wyraz wolny
    public static double R = 0; // współczynnik korelacji liniowej Pearsona
    public static double R2 = 0; //współczynnik determinacji liniowej
    public static void readFromCsv() throws IOException {

        InputStream inputStream = Main.class.getResourceAsStream("data.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.readLine(); //we don't want the first line as it is the table header not the data!
        String line;
        int i = 0;

        while((line = reader.readLine()) !=null) {
            System.out.println(line);
            String[] columns = line.split(",");
            x[i] = i+1;
            y[i] = Double.parseDouble(columns[1]);
            i++;
        }

    }

    public static void debugDataUtility(){
        /*Prints out the data from x and y arrays in formatted form*/
        System.out.println("Cena akcji, Dzień");
        IntStream.range(0, n)
                .forEach(i -> System.out.println(String.format("%.2f", x[i]) + (x[i]>=100? "   " : "    ") + y[i]));
        //if x[i] has 3 digits then 3 spaces, if it has 2 digits then 4 spaces so the space between x and y is constant
    }
    public static void regresjaLiniowa(double[] x, double[] y) {

        for (double i : x) x̄ += i;
        x̄ /= n;
        for (double i : y) ȳ += i;
        ȳ /= n;

        double[] xTrans = new double[n]; // x - x̄
        double[] yTrans = new double[n]; // y -  ȳ
        for (int i = 0; i < n; i++) {
            xTrans[i] = x[i] - x̄; //dla kazdego x liczymy x - sredniego x
            yTrans[i] = y[i] - ȳ; //dla kazdego y liczymy y - sredniego y
            System.out.println(xTrans[i] + "," + yTrans[i]);
        }

        double Sxy = 0;
        double Sx2 = 0;
        double Sy2 = 0;
        for (int i = 0; i < n; i++) {
            Sxy += xTrans[i] * yTrans[i]; // ( x - x̄ ) *  ( y -  ȳ )
            Sx2 += xTrans[i] * xTrans[i]; // ( x - x̄ ) ^2
            Sy2 += yTrans[i] * yTrans[i];
        }

        a = Sxy / Sx2; //suma xy / suma x^2
        b = ȳ - a * x̄; //sredni y - ax
        R = Sxy / (Math.sqrt(Sx2) * Math.sqrt(Sy2));
        R2 = R * R;


    }

    public static void doTheRegression(double[] x, double[] y, String nazwa_x, String nazwa_y) {

        // współrzędne x początku i końca linii regr.
        double x1 = min(x);
        double x2 = max(x);

        //wsp. y początku i końca linii regr.
        double y1 = a * x1 + b;
        double y2 = a * x2 + b;



        Plot2DPanel plot = new Plot2DPanel();
        plot.addLinePlot("nwm",x,y);
        plot.addScatterPlot("Scatter", x, y);
        plot.changePlotColor(0,new Color(0,0,120));

        plot.addLinePlot("", Color.BLACK,  new double[] {x1,y1}, new double[]{x2,y2});


        JFrame frame = new JFrame("Linear Regression");
        frame.setContentPane(plot);
        frame.setSize(600, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) throws IOException {
        readFromCsv();
        regresjaLiniowa(x,y);
        //debugDataUtility();
        doTheRegression(x, y, "dni", "cena akcji");
    }
}
