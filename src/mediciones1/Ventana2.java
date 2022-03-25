/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mediciones1;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Ventana2 {
        Conexion conexion = new Conexion();
    
    public void mostrar_grafica() throws Exception{
        
        // Crear ventana
        JFrame ventana = new JFrame();
        ventana.setTitle("Gráfica");
        ventana.setSize(600,400);
        ventana.setLayout(new BorderLayout());
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Crear botones de graficacion y salir
        JButton conectar = new JButton("Graficar");
        JButton salir = new JButton("Salir");
        JComboBox<String> lectList = new JComboBox<String>();
        
        JPanel topPanel = new JPanel();
        topPanel.add(lectList);
        topPanel.add(conectar);
        ventana.add(topPanel, BorderLayout.NORTH);
        
        JPanel southPanel = new JPanel();
        southPanel.add(salir);
        ventana.add(southPanel, BorderLayout.SOUTH);

        // LLenar lista de puertos COM
        conexion.conectar();
        conexion.select_datos_lecturas("SELECT DISTINCT lectura FROM datos");
        for(int i = 0; i < conexion.lecturas.size(); i++){
            lectList.addItem(conexion.lecturas.get(i));
        }
        
        // Crear grafica
        XYSeries series = new XYSeries("Valores de prueba");
        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYLineChart("Gráfica de prueba", "Tiempo (s)", "Litros (l)", dataset);
        ventana.add(new ChartPanel(chart), BorderLayout.CENTER);

        conectar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                
                if(conectar.getText().equals("Graficar")){
                    
                    conectar.setText("¿Volver a graficar?");
                    lectList.setEnabled(false);
                    
                    try {
                        conexion.conectar();
                    } catch (Exception ex) {
                        Logger.getLogger(Ventana2.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    conexion.select_datos("Select * from datos where lectura = '"+lectList.getSelectedItem().toString()+"'");

                    for(int i=0; i< conexion.id.size(); i++){
                        series.add(Double.parseDouble(conexion.tiempo.get(i)),Double.parseDouble(conexion.litros.get(i)));
                    }
                }
                
                else{
                    lectList.setEnabled(true);
                    conectar.setText("Graficar");
                    series.clear();
                } 
            }
        });
        
        salir.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                inicio inicio = new inicio();
                inicio.setLocationRelativeTo(null);
                inicio.setVisible(true);
                ventana.setVisible(false);
            }
        });
        
        dataset.addSeries(series);
        ventana.setVisible(true);
    }
}
