/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mediciones1;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.Date;
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

public class Ventana1 {
        static SerialPort chosenPort;
    static Conexion conexion = new Conexion();
    
    private javax.swing.JTextField recuadro;
    Date fecha;
    
    public void mostrar_grafica() throws Exception{

        // Crear ventana
        JFrame ventana = new JFrame();
        ventana.setTitle("Gráfica");
        ventana.setSize(600,400);
        ventana.setLayout(new BorderLayout());
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Crear boton de conexion
        JComboBox<String> portList = new JComboBox<String>();
        JButton connectionButton = new JButton("Conectar");
        JButton salir = new JButton("Salir");
        
        recuadro = new javax.swing.JTextField(10);
        
        JPanel topPanel = new JPanel();
        topPanel.add(portList);
        topPanel.add(connectionButton);
        topPanel.add(salir);
        ventana.add(topPanel, BorderLayout.NORTH);
        
        JPanel southPanel = new JPanel();
        recuadro.setMaximumSize(recuadro.getPreferredSize());
        southPanel.add(recuadro);
        ventana.add(southPanel, BorderLayout.SOUTH);
        
        // LLenar lista de puertos COM
        SerialPort[] portName = SerialPort.getCommPorts();
        for(int i = 0; i < portName.length; i++){
            portList.addItem(portName[i].getSystemPortName());
        }

        // Crear grafica
        XYSeries series = new XYSeries("Valores de prueba");
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createXYLineChart("Gráfica de prueba", "Tiempo (s)", "Litros (L)", dataset);
        ventana.add(new ChartPanel(chart), BorderLayout.CENTER);
        
        connectionButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
               if(connectionButton.getText().equals("Conectar")){
                   chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                   chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                   if(chosenPort.openPort()){
                       connectionButton.setText("Desconectar");
                       portList.setEnabled(false);
                       salir.setEnabled(false);
                       recuadro.setEnabled(false);
                       fecha = new Date();
                   }
                   
                   Thread hilo = new Thread(){
                    @Override public void run(){
                        
                        Scanner scanner = new Scanner(chosenPort.getInputStream());
                        
                        double x = 0;
                        
                        while(scanner.hasNextLine()){
                            try{
                                
                                try {
                                    conexion.conectar();
                                } catch (Exception ex) {
                                    Logger.getLogger(Ventana1.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                String line = scanner.nextLine();
                                double number = Double.parseDouble(line);
                                series.add(x=x+1,number);
                                
                                if(recuadro.getText().isEmpty()){
                                    conexion.insert_datos(fecha.toString(),number,x);
                                    System.out.println(fecha.toString()+" "+x+" "+number);
                                }
                                
                                else{
                                    conexion.insert_datos(recuadro.getText(),number,x);
                                    System.out.println(recuadro.getText()+" "+x+" "+number);
                                }
                                
                                
                            } 
                            catch(Exception e) {}
                        }
                        
                        scanner.close();
                    }
                   };
                   hilo.start();
               }
               else{
                   chosenPort.closePort();
                   portList.setEnabled(true);
                   salir.setEnabled(true);
                   recuadro.setEnabled(true);
                   connectionButton.setText("Conectar");
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
