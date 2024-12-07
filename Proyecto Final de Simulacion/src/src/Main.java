package src;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 */
public class Main extends javax.swing.JFrame {

    //Poblacion 2020 según el INEGI
    //private int poblacion = 4132148;
    private int poblacion;
    //Muertes por cancer en años: 2020, 2021,2022,2023,2024,proyección
    private int muertesCancer[];
    //Muerte por material particulado 2.5pm
    private double pmPorAnio[];
    private int poblacion2020;
    private int poblacion2021;
    private int poblacion2022;
    private double particula2020;
    private double particula2021;
    private double particula2022;
    private int muertesC2020;
    private int muertesC2021;
    private int muertesC2022;
    private int realC2020;
    private int realC2021;
    private int realC2022;
    private int proyeccionMuertesCancer;
    private double proyeccionPmPorAnio;
    //Concentración de PM 2.5 contrafactual MIN
    private final double x0_min = 5.8;

    //Concentración de PM 2.5 contrafactual MAX
    private final double x0_max = 8.8;

    private double x, tasaMC;

    //Crecimiento del RR por exposición de PM
    private final double beta = 0.2322;
    private JFreeChart chart;
    private ChartPanel panel;
    private DefaultCategoryDataset datosPM;

    public Main() {
        
        
        initComponents();

        jTabbedPane1.setEnabledAt(1, false);

        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Simulación Cáncer de Pulmón y PM 2.5");
        /*iniciarGrafica();

        panel = new ChartPanel(chart, false);

        panel.setBounds(10, 20, 760, 520);
        panelGrafica1.add(panel);*/
        
    }

    public void iniciarGrafica() {
  
        double numTotal = 0;
        //this.poblacion = 4132148;
        this.poblacion = Integer.parseInt(pob2020.getText());
        this.setTasaMC(0);
        this.x = pmPorAnio[0];

        double tasaMin = getTMCPMin();
        numTotal += tasaMin;
        double tasaMax = getTMCPMax();
        numTotal += tasaMax;
        this.textoMin2020.setText("Valor Real: " + realC2020);
        this.textoMax2020.setText("Valor aproximado simulado: " + tasaMax);

        datosPM = new DefaultCategoryDataset();

        
        //datosPM.setValue(tasaMin, "Real", "2020");
        datosPM.setValue(realC2020, "Real", "2020");
        datosPM.setValue(tasaMax, "Aproximado", "2020");
        
        
        //this.poblacion = 4132148;
        this.poblacion= Integer.parseInt(pob2021.getText());
        this.setTasaMC(1);
        this.x = pmPorAnio[1];
        tasaMin = getTMCPMin();
        numTotal += tasaMin;
        tasaMax = getTMCPMax();
        numTotal += tasaMax;

        //datosPM.setValue(tasaMin, "Real", "2021");
        datosPM.setValue(realC2021, "Real", "2021");
        datosPM.setValue(tasaMax, "Aproximado", "2021");
        
        this.textoMin2021.setText("Valor real: " + realC2021);
        this.textoMax2021.setText("Valor aproximado simulado: " + tasaMax);
        
        //this.poblacion = 4132148;
        this.poblacion = Integer.parseInt(pob2022.getText());
        this.setTasaMC(2);
        this.x = pmPorAnio[2];
        tasaMin = getTMCPMin();
        numTotal += tasaMin;
        tasaMax = getTMCPMax();
        numTotal += tasaMax;

        //datosPM.setValue(tasaMin, "Real", "2022");
        datosPM.setValue(realC2022,"Real", "2022");
        datosPM.setValue(tasaMax, "Aproximado", "2022");
        
        this.textoMin2022.setText("Valor real: " + realC2022);
        this.textoMax2022.setText("Valor aproximado simulado: " + tasaMax);
        this.labelTotal.setText("Hasta " + String.valueOf(Math.round(numTotal)) + " aproximadamente");
        
        this.poblacion = Integer.parseInt(proyeccionPoblacion.getText());
        this.setTasaMC(3);
        this.x = pmPorAnio[3];
        tasaMin = getTMCPMin();
        numTotal += tasaMin;
        tasaMax = getTMCPMax();
        numTotal += tasaMax;
        
        //datosPM.setValue(tasaMin, "Real", "Proyeccion");
        datosPM.setValue(0, "Real", "Proyeccion");
        datosPM.setValue(tasaMax, "Aproximado", "Proyeccion");
        
        
        this.textoMinProyeccion.setText("Valor real: " + tasaMin);
        this.textoMaxProyeccion.setText("Valor aproximado: " + tasaMax);
        this.labelTotal.setText("Hasta " + String.valueOf(Math.round(numTotal)) + " aproximadamente");

        chart = ChartFactory.createAreaChart(
                "Número de muertes por Cáncer de Pulmón causadas por PM2.5",//Título
                "Año",//Etiqueta x
                "Número de muertes",//Etiqueta Y
                datosPM, //Datos
                PlotOrientation.VERTICAL,
                true, true, false);
    }

    public void setTasaMC(int indMuertes) {
        BigDecimal tasa = (BigDecimal.valueOf((double) this.muertesCancer[indMuertes] / this.poblacion));
        this.tasaMC = tasa.doubleValue() * 100000;
    }

    public double getRRMin() {
        double riesgoRelativo = (Math.pow(this.x + 1, beta)) / (Math.pow(this.x0_min + 1, beta));
        return riesgoRelativo;
    }

    public double getRRMax() {
        double riesgoRelativo = (Math.pow(this.x + 1, beta)) / (Math.pow(this.x0_max + 1, beta));
        return riesgoRelativo;
    }

    public double getAFMin() {
        double RRMin = getRRMin();
        double AF = RRMin - (1 / RRMin);
        return AF;
    }

    public double getAFMax() {
        double RR = getRRMax();
        double AF = RR - (1 / RR);
        return AF;
    }

    public double getTMCPMin() {
        double TMCP = getAFMin() * this.tasaMC;
        return TMCP;
    }

    public double getTMCPMax() {
        double TMCP = getAFMax() * this.tasaMC;
        return TMCP;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        proyeccionMuertes = new javax.swing.JTextField();
        proyeccionPM = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        Salir1 = new javax.swing.JButton();
        proyeccionPoblacion = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        muertes2020 = new javax.swing.JTextField();
        pm2020 = new javax.swing.JTextField();
        pob2020 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        muertes2021 = new javax.swing.JTextField();
        pm2021 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        pob2021 = new javax.swing.JTextField();
        muertes2022 = new javax.swing.JTextField();
        pm2022 = new javax.swing.JTextField();
        pob2022 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        real2020 = new javax.swing.JTextField();
        real2021 = new javax.swing.JTextField();
        real2022 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelGrafica1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        textoMin2020 = new javax.swing.JLabel();
        textoMax2020 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        textoMin2021 = new javax.swing.JLabel();
        textoMax2021 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        textoMin2022 = new javax.swing.JLabel();
        textoMax2022 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        textoMinProyeccion = new javax.swing.JLabel();
        textoMaxProyeccion = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        SalirG = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(204, 255, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("Simulación para la Estimación de Muertes por Cáncer de Pulmón Asociadas por Contaminación Ambiental de PM2.5");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("El objetivo del estudio es estimar el número de muertes por cáncer de pulmón provocadas por la contaminación ambiental debida a la exposición de las personas a material particulado fino menor a  2.5 µm.");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Dicho material está presente en el aire en forma sólida o líquida. ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Las fórmulas que usa la simulación son:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("* El análisis de estimación en función de la distribución empírica del Riesgo Relativo (R.R) y la tasa de mortalidad. ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Los datos arrojados en la primer pestaña nos dirá la cantidad de personas que han muerto a causa de la contaminación del aire por esta partícula.");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Fracción Atribuible (AF)");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("La simulación tiene como punto principal:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/riesgorelativo.png"))); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("Riesgo relativo (RR) ");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/fraccionAtribuible.png"))); // NOI18N

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Número de muertes por cáncer de pulmón");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/tasadeMuertesPM.png"))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("asociadas a PM 2.5");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lib/tasaMuertes.png"))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Tasa de Muertes por Cáncer de Pulmón");

        jButton1.setText("Mostrar Simulación");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        proyeccionMuertes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proyeccionMuertesActionPerformed(evt);
            }
        });

        jLabel26.setText("Introducir datos para la simulación");

        jLabel27.setText("Introducir número de muertes por cáncer de pulmón por año");

        jLabel28.setText("Introducir PM2.5 por año");

        Salir1.setText("Salir del Programa");
        Salir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Salir1ActionPerformed(evt);
            }
        });

        jLabel29.setText("Introducir población para la proyección");

        jLabel23.setText("Datos 2020");

        muertes2020.setText("306");
        muertes2020.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                muertes2020ActionPerformed(evt);
            }
        });

        pm2020.setText("16.9");

        pob2020.setText("4132148");

        jLabel24.setText("Datos 2021");

        muertes2021.setText("304");

        pm2021.setText("17.2");

        jLabel30.setText("Ingresar los datos");

        jLabel31.setText("Muertes por C.P.");

        jLabel32.setText("PM 2.5 por año");

        jLabel33.setText("Poblacion por año");

        pob2021.setText("4132148");

        muertes2022.setText("316");

        pm2022.setText("17.76");

        pob2022.setText("4132148");

        jLabel34.setText("Datos 2022");

        jLabel35.setText("Valor real");

        real2020.setText("3");

        real2021.setText("3");

        real2022.setText("4");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(proyeccionPoblacion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29))
                    .addComponent(jLabel1)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel14)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(52, 52, 52)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addComponent(jLabel16)
                                .addGap(148, 148, 148)
                                .addComponent(jLabel11))
                            .addComponent(jLabel22)
                            .addComponent(jLabel21)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(real2020, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(real2021))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel30)
                                    .addComponent(jLabel31)
                                    .addComponent(jLabel32)
                                    .addComponent(jLabel33))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pm2020)
                                    .addComponent(pob2020)
                                    .addComponent(jLabel23)
                                    .addComponent(muertes2020))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel24)
                                    .addComponent(pm2021)
                                    .addComponent(pob2021)
                                    .addComponent(muertes2021)))
                            .addComponent(jLabel19)
                            .addComponent(jLabel18)
                            .addComponent(jLabel20))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pm2022)
                            .addComponent(pob2022)
                            .addComponent(jLabel34)
                            .addComponent(muertes2022)
                            .addComponent(real2022, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(proyeccionPM, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel28))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(proyeccionMuertes, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel27)))
                        .addGap(37, 37, 37)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Salir1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(316, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(32, 32, 32)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addGap(160, 160, 160))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(66, 66, 66)
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel11)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel17))
                        .addGap(26, 26, 26)))
                .addComponent(jLabel22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel21))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel24)
                            .addComponent(jLabel30)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(muertes2020, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(muertes2021, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)
                            .addComponent(muertes2022, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pm2020, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pm2021, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32)
                            .addComponent(pm2022, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pob2020, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)
                            .addComponent(pob2021, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pob2022, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(real2020, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(real2021, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(real2022, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(proyeccionMuertes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(proyeccionPM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Salir1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(proyeccionPoblacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Información del modelo", jPanel6);

        jPanel7.setForeground(new java.awt.Color(255, 204, 204));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel5.setText("Simulación para la Estimación de Muertes por Cáncer de Pulmón Asociadas por Contaminación Ambiental de PM2.5");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Resultados obtenidos");

        javax.swing.GroupLayout panelGrafica1Layout = new javax.swing.GroupLayout(panelGrafica1);
        panelGrafica1.setLayout(panelGrafica1Layout);
        panelGrafica1Layout.setHorizontalGroup(
            panelGrafica1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
        );
        panelGrafica1Layout.setVerticalGroup(
            panelGrafica1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );

        jLabel2.setText("Muertes por Cáncer de Pulmón asociadas contaminación del aire en 2020:");

        textoMin2020.setText("asd");

        textoMax2020.setText("asd");

        jLabel3.setText("Muertes por Cáncer de Pulmón asociadas contaminación del aire en 2021:");

        textoMin2021.setText("asd");

        textoMax2021.setText("asd");

        jLabel6.setText("Muertes por Cáncer de Pulmón asociadas contaminación del aire en 2022:");

        textoMin2022.setText("asd");

        textoMax2022.setText("asd");

        jLabel4.setText("Total de muertes asociadas a PM2.5:");

        labelTotal.setText("asd");

        jLabel25.setText("Proyeccion de Muertes por Cáncer de Pulmón asociadas contaminación del aire:");

        textoMinProyeccion.setText("asd");

        textoMaxProyeccion.setText("asd");

        jButton2.setText("Regresar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        SalirG.setText("Salir del programa");
        SalirG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2)
                    .addComponent(textoMin2020)
                    .addComponent(textoMax2020)
                    .addComponent(jLabel3)
                    .addComponent(textoMax2021)
                    .addComponent(jLabel6)
                    .addComponent(textoMin2022)
                    .addComponent(textoMax2022)
                    .addComponent(jLabel4)
                    .addComponent(labelTotal)
                    .addComponent(textoMin2021)
                    .addComponent(jLabel25)
                    .addComponent(textoMinProyeccion)
                    .addComponent(textoMaxProyeccion)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(SalirG)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelGrafica1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(168, 168, 168))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(jLabel5)
                .addContainerGap(470, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGrafica1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMin2020)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMax2020)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMin2021)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMax2021)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMin2022)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMax2022)
                        .addGap(156, 156, 156)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMinProyeccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textoMaxProyeccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(SalirG))))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Simulación por Año", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        this.muertesC2020=Integer.parseInt(this.muertes2020.getText());
        this.muertesC2021=Integer.parseInt(this.muertes2021.getText());
        this.muertesC2022=Integer.parseInt(this.muertes2022.getText());
        this.particula2020=Double.parseDouble(this.pm2020.getText());
        this.particula2021=Double.parseDouble(this.pm2021.getText());
        this.particula2022=Double.parseDouble(this.pm2022.getText());
        this.realC2020=Integer.parseInt(this.real2020.getText());
        this.realC2021=Integer.parseInt(this.real2021.getText());
        this.realC2022=Integer.parseInt(this.real2022.getText());
        
        this.proyeccionMuertesCancer = Integer.parseInt(this.proyeccionMuertes.getText());
        this.proyeccionPmPorAnio = Double.parseDouble(this.proyeccionPM.getText());
        
        //this.pmPorAnio = new double[]{16.9, 17.2, 17.76, proyeccionPmPorAnio};
        this.pmPorAnio = new double[]{particula2020,particula2021,particula2022,proyeccionPmPorAnio};
        //this.muertesCancer = new int[]{306, 304, 316, proyeccionMuertesCancer};
        this.muertesCancer = new int[]{muertesC2020,muertesC2021,muertesC2022,proyeccionMuertesCancer};
        
        iniciarGrafica();

        panel = new ChartPanel(chart, false);

        panel.setBounds(10, 20, 760, 520);
        panelGrafica1.add(panel);
        jTabbedPane1.setEnabledAt(1, true);
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void proyeccionMuertesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proyeccionMuertesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_proyeccionMuertesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new Main().setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void Salir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Salir1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_Salir1ActionPerformed

    private void SalirGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirGActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_SalirGActionPerformed

    private void muertes2020ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_muertes2020ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_muertes2020ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        try {
            FlatLightFlatIJTheme.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Salir1;
    private javax.swing.JButton SalirG;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JTextField muertes2020;
    private javax.swing.JTextField muertes2021;
    private javax.swing.JTextField muertes2022;
    private javax.swing.JPanel panelGrafica1;
    private javax.swing.JTextField pm2020;
    private javax.swing.JTextField pm2021;
    private javax.swing.JTextField pm2022;
    private javax.swing.JTextField pob2020;
    private javax.swing.JTextField pob2021;
    private javax.swing.JTextField pob2022;
    private javax.swing.JTextField proyeccionMuertes;
    private javax.swing.JTextField proyeccionPM;
    private javax.swing.JTextField proyeccionPoblacion;
    private javax.swing.JTextField real2020;
    private javax.swing.JTextField real2021;
    private javax.swing.JTextField real2022;
    private javax.swing.JLabel textoMax2020;
    private javax.swing.JLabel textoMax2021;
    private javax.swing.JLabel textoMax2022;
    private javax.swing.JLabel textoMaxProyeccion;
    private javax.swing.JLabel textoMin2020;
    private javax.swing.JLabel textoMin2021;
    private javax.swing.JLabel textoMin2022;
    private javax.swing.JLabel textoMinProyeccion;
    // End of variables declaration//GEN-END:variables
}
