package dr.app.tracer.traces;

import javax.swing.*;
import java.awt.*;

/**
 * @author Walter Xie
 */
public class FilterContinuousPanel extends FilterPanel {
    JTextField minField;
    JTextField maxField;

    FilterContinuousPanel(String[] minMax, String[] bound) {
        setLayout(new GridLayout(2, 3, 1, 10)); // 2 by 3, gap 5 by 1

        if (bound == null) {
            bound = new String[2];
        }

        minField = new JTextField(bound[0]);
        minField.setColumns(20);
        add(new JLabel("Set Minimum for Selecting Values : "));
        add(minField);
        add(new JLabel(", which should > " + minMax[0]));

        maxField = new JTextField(bound[1]);
        maxField.setColumns(20);
        add(new JLabel("Set Maximum for Selecting Values : "));
        add(maxField);
        add(new JLabel(", which should < " + minMax[1]));
    }

    public Object[] getSelectedValues() {
        return new String[]{minField.getText(), maxField.getText()};
    }

}