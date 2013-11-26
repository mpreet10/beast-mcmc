package dr.evomodel.epidemiology.casetocase;

import dr.evolution.util.TaxonList;
import dr.evomodel.coalescent.AbstractCoalescentLikelihood;
import dr.evomodel.tree.TreeModel;
import dr.inference.model.Parameter;

/**
 * Intended to replace the tree prior; each partition is considered a tree in its own right generated by a
 * coalescent process
 *
 * @author Matthew Hall
 */

public class WithinCaseCoalescent extends CaseToCaseTreeLikelihood {

    public static final String WITHIN_CASE_COALSECENT = "withinCaseCoalescent";
    private AbstractCoalescentLikelihood coalescentLikelihood;

    public WithinCaseCoalescent(TreeModel virusTree, AbstractOutbreak caseData, String startingNetworkFileName,
                                    Parameter infectionTimeBranchPositions, Parameter maxFirstInfToRoot,
                                    boolean extended, boolean normalise, boolean jeffreys)
            throws TaxonList.MissingTaxonException {
        super(WITHIN_CASE_COALSECENT, virusTree, caseData, startingNetworkFileName, infectionTimeBranchPositions,
                maxFirstInfToRoot, extended, normalise, jeffreys);
    }


}
