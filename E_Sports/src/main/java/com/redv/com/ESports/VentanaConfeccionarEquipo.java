package com.redv.com.ESports;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class VentanaConfeccionarEquipo {
    private JButton cancelarButton;
    private JButton confeccionarButton;
    private JComboBox jugador1;
    private JComboBox jugador3;
    private JComboBox jugador2;
    private JComboBox jugador4;
    private JComboBox jugador5;
    private JComboBox jugador6;
    private JLabel TextoNombreEquipo;
    private JPanel VentanaConfeccionarEquipo;
    private JTextField nomEquipo;
    private JLabel salarioTotal;

    private EquipoBD equipoBD = new EquipoBD();
    private String nombreEquipo;

    public VentanaConfeccionarEquipo() {
        JFrame frame = new JFrame("VentanaConfeccionarEquipo");
        frame.setContentPane(VentanaConfeccionarEquipo);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //Nombre Equipo aquí
        nombreEquipo = nomEquipo.getText().toUpperCase().trim();

        //cargar lista jugadores aquí
        List<Jugador> jugadoresDisp = equipoBD.cargarJugadoresDisponibles();
        cargarCombos(jugadoresDisp);

        confeccionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int contador = 0;
                boolean creado;

                //gestionar nombre de equipo no introducido
                if (nomEquipo.getText().trim().equalsIgnoreCase("")) {
                    System.out.println("Equipo no introducido");
                    TextoNombreEquipo.setForeground(Color.RED);

                } else {

                    List<Jugador> jugadoresEleg = new ArrayList<>();

                    if (jugador1.getSelectedItem() != null) {    //TODO: SI NO SELECCIONA NADA DEVUELVE NULL??
                        jugadoresEleg.add((Jugador) jugador1.getSelectedItem());
                    }

                    if (jugador2.getSelectedItem() != null) {
                        jugadoresEleg.add((Jugador) jugador2.getSelectedItem());
                    }

                    if (jugador3.getSelectedItem() != null) {
                        jugadoresEleg.add((Jugador) jugador3.getSelectedItem());
                    }

                    if (jugador4.getSelectedItem() != null) {
                        jugadoresEleg.add((Jugador) jugador4.getSelectedItem());
                    }

                    if (jugador5.getSelectedItem() != null) {
                        jugadoresEleg.add((Jugador) jugador5.getSelectedItem());
                    }

                    if (jugador6.getSelectedItem() != null) {
                        jugadoresEleg.add((Jugador) jugador6.getSelectedItem());
                    }

                    Set<Jugador> jugNoRep = new TreeSet<>();

                    for (Jugador j : jugadoresEleg) {
                        if (jugNoRep.contains(j)) {
                            System.out.println("Error: Jugador repetido");
                            JOptionPane.showMessageDialog(VentanaConfeccionarEquipo, "No es posible elegir al mismo jugador más de una vez: " + j.getNickname(), "¡Jugador Repetido!", JOptionPane.ERROR_MESSAGE);
                        } else {
                            jugNoRep.add(j);
                        }
                    }

                    //gestión salario máximo
                    double salTotal = 0;
                    for (Jugador j : jugNoRep) {
                        salTotal += j.getSalario();
                        salarioTotal.setText("Salario Total: " + String.valueOf(salTotal)); //TODO: LISTENER PARA CALCULAR SALARIO CADA VEZ QUE SE ELIJE UN JUGADOR
                    }

                    if (salTotal <= 200000) {
                        //actualizar el campo equipo de los jugadoresEleg en BD
                        for (Jugador j : jugNoRep) {
                            creado = equipoBD.confeccionarEquipo(j, nombreEquipo);
                            if (creado) {
                                contador++;
                            }
                        }
                        if (contador == jugNoRep.size()) {
                            JOptionPane.showMessageDialog(VentanaConfeccionarEquipo, "¡Equipo creado! + \n" + nombreEquipo, "Resultado", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            for (Jugador j : jugNoRep) {
                                equipoBD.rollBack(j);   //deshacemos los cambios si todos los jugadores elegidos no han sido actualizados correctamente en BD
                            }
                            JOptionPane.showMessageDialog(VentanaConfeccionarEquipo, "¡Equipo NO creado! + \n" + nombreEquipo + "\n" + "Consulte con Administrador", "Resultado", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        salarioTotal.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(VentanaConfeccionarEquipo, "¡Presupuesto para salarios superado en: " + nombreEquipo + "!", "Resultado", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    public void cargarCombos(List<Jugador> jugadoresDisp) {
        DefaultComboBoxModel<Jugador> modelo = new DefaultComboBoxModel<>();

        for (Jugador j : jugadoresDisp) {
            modelo.addElement(j);
        }

        jugador1.setModel(modelo); //TODO: PREGUNTAR A ION FALLO UNCHECKED CALL
        jugador2.setModel(modelo);
        jugador3.setModel(modelo);
        jugador4.setModel(modelo);
        jugador5.setModel(modelo);
        jugador6.setModel(modelo);

    }
}
