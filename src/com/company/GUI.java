package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static java.awt.Color.GRAY;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class GUI {

    public GUI(final String datesFile, final Optional<ArrayList<LocalDate>> initialDates) {
        JFrame f = new JFrame();

        final var GREEN = new Color(0x216E39);
        var daysToBeginOfWeek = now().getDayOfWeek().getValue();
        var firstDay = now().minusDays(daysToBeginOfWeek);
        var lastDay = now().plusYears(1);
        var dates = new ArrayList<>(initialDates.get());
        for (int i = 1, j = 10; (i-1) <= DAYS.between(firstDay, lastDay); i++){

            int yPosition = 18 * (i <= 7? i: (i - (7 * ((i - 1) / 7))));
            final var date = i;
            JButton b = new JButton("") {{
                setBackground(GRAY);
                setOpaque(true);
                setBorderPainted(false);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton button = (JButton) e.getSource();
                        setBackground(getBackground() == GRAY? GREEN: GRAY);

                        var date = LocalDate.parse(button.getActionCommand());

                        if(dates.stream().filter(d -> d.isEqual(date)).findAny().isEmpty()){
                            dates.add(date);
                        }else{
                            dates.removeIf(d -> d.isEqual(date));
                        }
                        System.out.println(dates);
                    }
                });
            }};

            var buttonDate = firstDay.plusDays(i-1);

            if(initialDates.isPresent() && initialDates.get().stream().anyMatch(d -> d.isEqual(buttonDate)) ){
                b.setBackground(GREEN);
            }

            b.setActionCommand(buttonDate.toString());
            b.setToolTipText(i + " - " + firstDay.plusDays(i-1));
            b.setBounds(j,yPosition,13, 13 );

            f.add(b);
            if(i > 0 && i % 7 == 0){
                j += 18;
            }
        }

        JButton generate = new JButton("Generate Commit Sheet");

        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Serializer.serialize(dates, datesFile);
                JOptionPane.showMessageDialog (null, "Commit sheet created.");
            }
        });

        generate.setBounds(10,160,200, 20 );
        f.add(generate);
        f.setSize(1000,500);
        f.setLayout(null);
        f.setVisible(true);

    }
}
