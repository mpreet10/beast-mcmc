package dr.evomodel.MSSD;

import dr.evolution.tree.Tree;
import dr.evomodel.tree.TreeModel;
import dr.inference.model.AbstractModelLikelihood;
import dr.inference.model.Likelihood;
import dr.inference.model.Model;
import dr.inference.model.Parameter;
import dr.math.GammaFunction;
import dr.xml.*;

/**
 * Package: alsDefaultPrior
 * Description:
 * <p/>
 * <p/>
 * Created by
 * Alexander V. Alekseyenko (alexander.alekseyenko@gmail.com)
 * Date: Mar 14, 2008
 * Time: 12:47:07 PM
 */
public class ExponentialBranchLengthTreePrior extends AbstractModelLikelihood {

    TreeModel treeModel;

    public ExponentialBranchLengthTreePrior(TreeModel treeModel) {
        super(null);
        this.treeModel = treeModel;
    }

    public static final String MODEL_NAME = "exponentialBranchLengthsPrior";
    public static final String TREEMODEL = "treeModel";

    /**
     * The XML parser
     */
    public static XMLObjectParser PARSER = new AbstractXMLObjectParser() {

        public String getParserName() {
            return MODEL_NAME;
        }

        public Object parseXMLObject(XMLObject xo) throws XMLParseException {

            TreeModel treeModel = (TreeModel) xo.getChild(TreeModel.class);

            return new ExponentialBranchLengthTreePrior(treeModel);
        }

        //************************************************************************
        // AbstractXMLObjectParser implementation
        //************************************************************************

        public String getParserDescription() {
            return "This element represents a tree prior assuming " +
                    "exponentially distributed branch lengths.";
        }

        public Class getReturnType() {
            return Likelihood.class;
        }

        public XMLSyntaxRule[] getSyntaxRules() {
            return rules;
        }

        private XMLSyntaxRule[] rules = new XMLSyntaxRule[]{
                new ElementRule(TreeModel.class),
        };
    };

    protected void handleModelChangedEvent(Model model, Object object, int index) {
//AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * This method is called whenever a parameter is changed.
     * <p/>
     * It is strongly recommended that the model component sets a "dirty" flag and does no
     * further calculations. Recalculation is typically done when the model component is asked for
     * some information that requires them. This mechanism is 'lazy' so that this method
     * can be safely called multiple times with minimal computational cost.
     */
    protected final void handleParameterChangedEvent(Parameter parameter, int index, Parameter.ChangeType type) {
//AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * Additional state information, outside of the sub-model is stored by this call.
     */
    protected void storeState() {
//AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * After this call the model is guaranteed to have returned its extra state information to
     * the values coinciding with the last storeState call.
     * Sub-models are handled automatically and do not need to be considered in this method.
     */
    protected void restoreState() {
//AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * This call specifies that the current state is accept. Most models will not need to do anything.
     * Sub-models are handled automatically and do not need to be considered in this method.
     */
    protected void acceptState() {
//AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * Get the model.
     *
     * @return the model.
     */
    public Model getModel() {
        return this;  //AUTOGENERATED METHOD IMPLEMENTATION
    }

    /**
     * Get the log likelihood.
     *
     * @return the log likelihood.
     */
    public double getLogLikelihood() {
        return calculateLogLikelihood();
    }

    public double calculateLogLikelihood() {
        int L = treeModel.getNodeCount();

        double totalTreeTime = Tree.Utils.getTreeLength(treeModel, treeModel.getRoot());

//        if(ctmcScale != null){ //
//            double ab=ctmcScale.getParameterValue(0);
//            return GammaFunction.lnGamma(L)-Math.log(mu*lam)-(L-1)*Math.log(totalTreeTime)-0.5*Math.log(ab)-ab*totalTreeTime;
//        }else{ // No Markov Chain for this model
        return GammaFunction.lnGamma(L) - (L - 1) * Math.log(totalTreeTime);
    }

    /**
     * Forces a complete recalculation of the likelihood next time getLikelihood is called
     */
    public void makeDirty() {
//AUTOGENERATED METHOD IMPLEMENTATION
    }
}
