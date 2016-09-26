/**
 * Created by Carlos Escobar on 9/19/2016.
 */

package RegExTreeBuilder

import RegExTreeBuilder.TreeBuilder.*
import RegExTreeBuilder.TreeDefinition.*

class RegExTreeBuilder{
    val regExParser:RegularExpressionTreeBuilder = RegularExpressionTreeBuilder()

    fun BuildRegExTree(regEx: String): Any{
        if(regEx.length == 1){
            return regExParser.Parse(regEx) as CharNode
        } else if ((regEx.get(regEx.length-1).equals('*')) || (regEx.get(regEx.length-2).equals('*'))){
            return regExParser.Parse(regEx) as RepeatNode
        } else if((regEx.contains('.')) && (!regEx.contains('+')) && (!regEx.contains('*'))){
            return regExParser.Parse(regEx) as ANDNode
        } else if((regEx.contains('+')) && (!regEx.contains('.')) && (!regEx.contains('*'))){
            return regExParser.Parse(regEx) as ORNode
        } else if(regEx.get(0).equals('(') && regEx.get(1).equals('(')){
            var numberOfInitialParenthesis = 0
            while(regEx.get(numberOfInitialParenthesis).equals('(')){
                numberOfInitialParenthesis++
            }
            numberOfInitialParenthesis--

            var pos = 0
            while(numberOfInitialParenthesis>0){
                if(regEx.get(pos).equals(')')){
                    numberOfInitialParenthesis--
                }
                pos++
            }

            if(regEx.get(pos).equals('.')){
                return regExParser.Parse(regEx) as ANDNode
            } else if(regEx.get(pos).equals('+')){
                return regExParser.Parse(regEx) as ORNode
            }
        } else if (regEx.get(regEx.length-1).equals(')') && regEx.get(regEx.length-2).equals(')')){
            var numberOfClosingParenthesis = 0
            var regExPos = regEx.length-1
            while(regEx.get(regExPos).equals(')')){
                regExPos--
                numberOfClosingParenthesis++
            }
            numberOfClosingParenthesis--

            regExPos = regEx.length-1
            while (numberOfClosingParenthesis>0){
                if(regEx.get(regExPos).equals('(')){
                    numberOfClosingParenthesis--
                }
                regExPos--
            }

            if(regEx.get(regExPos).equals('.')){
                return regExParser.Parse(regEx) as ANDNode
            } else if(regEx.get(regExPos).equals('+')){
                return regExParser.Parse(regEx) as ORNode
            }
        }
        return CharNode("")
    }

}