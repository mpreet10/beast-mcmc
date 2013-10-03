/*
 * CountableMixtureBranchRates.java
 *
 * Copyright (c) 2002-2013 Alexei Drummond, Andrew Rambaut and Marc Suchard
 *
 * This file is part of BEAST.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership and licensing.
 *
 * BEAST is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 *  BEAST is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BEAST; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301  USA
 */

package dr.evomodel.branchratemodel;

import dr.evolution.tree.NodeRef;
import dr.evolution.tree.Tree;
import dr.evomodel.tree.TreeModel;
import dr.evomodelxml.branchratemodel.CountableMixtureBranchRatesParser;
import dr.inference.loggers.LogColumn;
import dr.inference.loggers.Loggable;
import dr.inference.loggers.NumberColumn;
import dr.inference.model.Model;
import dr.inference.model.Parameter;
import dr.inference.model.Variable;

/**
 * @author Marc Suchard
 * @author Philippe Lemey
 */
public class CountableMixtureBranchRates extends AbstractBranchRateModel implements Loggable {

    private final Parameter ratesParameter;
    private final TreeModel treeModel;
    private final AbstractBranchRateModel randomEffectsModel;
    private final int categoryCount;

    public CountableMixtureBranchRates(CountableBranchCategoryProvider rateCategories,
                                       TreeModel treeModel, Parameter ratesParameter,
                                       AbstractBranchRateModel randomEffects, boolean inLogSpace) {
        super(CountableMixtureBranchRatesParser.COUNTABLE_CLOCK_BRANCH_RATES);

        this.treeModel = treeModel;
        categoryCount = ratesParameter.getDimension();
        this.rateCategories = rateCategories;
        rateCategories.setCategoryCount(categoryCount);

        if (rateCategories instanceof Model) {
            addModel((Model)rateCategories);
        }
        this.ratesParameter = ratesParameter;
        addVariable(ratesParameter);

        // Handle random effects
        this.randomEffectsModel = randomEffects;
        if (randomEffectsModel != null) {
            addModel(randomEffectsModel);
        }
        // TODO Check that randomEffectsModel mean is zero

        modelInLogSpace = inLogSpace;
    }

    public double getLogLikelihood() {
        return (randomEffectsModel != null) ? randomEffectsModel.getLogLikelihood() : 0.0;
    }

    public LogColumn[] getColumns() {
        LogColumn[] columns = new LogColumn[ratesParameter.getDimension()];
        for (int i = 0; i < ratesParameter.getDimension(); ++i) {
            columns[i] = new OccupancyColumn(i);
        }

        return columns;
    }

    private class OccupancyColumn extends NumberColumn {
        private final int index;

        public OccupancyColumn(int index) {
            super("Occupancy");
            this.index = index;
        }

        public double getDoubleValue() {
            int occupancy = 0;
            for (NodeRef node : treeModel.getNodes()) {
                if (node != treeModel.getRoot()) {
                    if (rateCategories.getBranchCategory(treeModel, node) == index) {
                        occupancy++;
                    }
                }
            }
            return occupancy;
        }
    }

    public void handleModelChangedEvent(Model model, Object object, int index) {
        if (model == rateCategories) {
            fireModelChanged();
        } else if (model == randomEffectsModel) {
            if (object == randomEffectsModel) {
                fireModelChanged();
            } else if (object == null) {
                fireModelChanged(null, index);
            } else {
                throw new IllegalArgumentException("Unknown object component!");
            }
        } else {
            throw new IllegalArgumentException("Unknown model component!");
        }
    }

    protected final void handleVariableChangedEvent(Variable variable, int index, Parameter.ChangeType type) {
        fireModelChanged();
    }

    protected void storeState() {
        // nothing to do
    }

    protected void restoreState() {
        // nothing to do
    }

    protected void acceptState() {
        // nothing to do
    }

    public double getBranchRate(final Tree tree, final NodeRef node) {

        assert !tree.isRoot(node) : "root node doesn't have a rate!";

        int rateCategory = rateCategories.getBranchCategory(tree, node);
        double effect = ratesParameter.getParameterValue(rateCategory);
        if (randomEffectsModel != null) {
            if (modelInLogSpace) {
                effect += randomEffectsModel.getBranchRate(tree, node);
            } else {
                effect *= randomEffectsModel.getBranchRate(tree, node);
            }
        }
        if (modelInLogSpace) {
            effect = Math.exp(effect);
        }
        return effect;
    }

    private final CountableBranchCategoryProvider rateCategories;
    private final boolean modelInLogSpace;
}