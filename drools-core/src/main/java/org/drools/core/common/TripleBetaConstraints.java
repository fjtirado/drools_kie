/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.drools.core.common;

import java.util.List;
import java.util.Optional;

import org.drools.base.base.ObjectType;
import org.drools.base.rule.ContextEntry;
import org.drools.base.rule.MutableTypeConstraint;
import org.drools.base.rule.Pattern;
import org.drools.base.rule.constraint.BetaNodeFieldConstraint;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.reteoo.Tuple;
import org.drools.core.reteoo.builder.BuildContext;
import org.drools.util.bitmask.BitMask;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.conf.IndexPrecedenceOption;

public class TripleBetaConstraints extends MultipleBetaConstraint {

    private static final long             serialVersionUID = 510l;

    public TripleBetaConstraints() { }

    public TripleBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                 final RuleBaseConfiguration conf) {
        this(constraints,
                conf,
                false);
    }

    public TripleBetaConstraints(final BetaNodeFieldConstraint[] constraints,
                                 final RuleBaseConfiguration conf,
                                 final boolean disableIndexing) {
        super(constraints, conf, disableIndexing);
    }

    protected TripleBetaConstraints( BetaNodeFieldConstraint[] constraints,
                                   IndexPrecedenceOption indexPrecedenceOption,
                                   boolean disableIndexing) {
        super(constraints, indexPrecedenceOption, disableIndexing);
    }

    public TripleBetaConstraints cloneIfInUse() {
        if (constraints[0] instanceof MutableTypeConstraint && ((MutableTypeConstraint)constraints[0]).setInUse()) {
            BetaNodeFieldConstraint[] clonedConstraints = new BetaNodeFieldConstraint[constraints.length];
            for (int i = 0; i < constraints.length; i++) {
                clonedConstraints[i] = constraints[i].cloneIfInUse();
            }
            TripleBetaConstraints clone = new TripleBetaConstraints(clonedConstraints, indexPrecedenceOption, disableIndexing);
            clone.indexed = indexed;
            return clone;
        }
        return this;
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#updateFromTuple(org.kie.reteoo.ReteTuple)
     */
    public void updateFromTuple(final ContextEntry[] context,
                                final ReteEvaluator reteEvaluator,
                                final Tuple tuple) {
        context[0].updateFromTuple(reteEvaluator,
                tuple);
        context[1].updateFromTuple(reteEvaluator,
                tuple);
        context[2].updateFromTuple(reteEvaluator,
                tuple);
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#updateFromFactHandle(org.kie.common.FactHandle)
     */
    public void updateFromFactHandle(final ContextEntry[] context,
                                     final ReteEvaluator reteEvaluator,
                                     final FactHandle handle) {
        context[0].updateFromFactHandle(reteEvaluator,
                handle);
        context[1].updateFromFactHandle(reteEvaluator,
                handle);
        context[2].updateFromFactHandle(reteEvaluator,
                handle);
    }

    public void resetTuple(final ContextEntry[] context) {
        context[0].resetTuple();
        context[1].resetTuple();
        context[2].resetTuple();
    }

    public void resetFactHandle(final ContextEntry[] context) {
        context[0].resetFactHandle();
        context[1].resetFactHandle();
        context[2].resetFactHandle();
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#isAllowedCachedLeft(java.lang.Object)
     */
    public boolean isAllowedCachedLeft(final ContextEntry[] context,
                                       final FactHandle handle) {
        return (indexed[0] || constraints[0].isAllowedCachedLeft(context[0], handle)) &&
               (indexed[1] || constraints[1].isAllowedCachedLeft(context[1], handle)) &&
               (indexed[2] || constraints[2].isAllowedCachedLeft( context[2], handle ));
    }

    /* (non-Javadoc)
     * @see org.kie.common.BetaNodeConstraints#isAllowedCachedRight(org.kie.reteoo.ReteTuple)
     */
    public boolean isAllowedCachedRight(final ContextEntry[] context,
                                        final Tuple tuple) {
        return constraints[0].isAllowedCachedRight( tuple, context[0] ) &&
               constraints[1].isAllowedCachedRight( tuple, context[1] ) &&
               constraints[2].isAllowedCachedRight( tuple, context[2] );
    }

    public int hashCode() {
        return constraints[0].hashCode() ^ constraints[1].hashCode() ^ constraints[2].hashCode();
    }

    /**
     * Determine if another object is equal to this.
     *
     * @param object
     *            The object to test.
     *
     * @return <code>true</code> if <code>object</code> is equal to this,
     *         otherwise <code>false</code>.
     */
    public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }

        if ( object == null || getClass() != object.getClass() ) {
            return false;
        }

        final TripleBetaConstraints other = (TripleBetaConstraints) object;

        if ( constraints[0] != other.constraints[0] && !constraints[0].equals(other.constraints[0]) ) {
            return false;
        }

        if ( constraints[1] != other.constraints[1] && !constraints[1].equals(other.constraints[1]) ) {
            return false;
        }

        if ( constraints[2] != other.constraints[2] && !constraints[2].equals(other.constraints[2]) ) {
            return false;
        }

        return true;
    }

    public BetaConstraints getOriginalConstraint() {
        throw new UnsupportedOperationException();
    }

    public BitMask getListenedPropertyMask(Pattern pattern, ObjectType modifiedType, List<String> settableProperties) {
        return constraints[0].getListenedPropertyMask(Optional.of(pattern), modifiedType, settableProperties)
                             .setAll(constraints[1].getListenedPropertyMask(Optional.of(pattern), modifiedType, settableProperties))
                             .setAll(constraints[2].getListenedPropertyMask(Optional.of(pattern), modifiedType, settableProperties));
    }

    public void registerEvaluationContext(BuildContext buildContext) {
        this.constraints[0].registerEvaluationContext(buildContext);
        this.constraints[1].registerEvaluationContext(buildContext);
        this.constraints[2].registerEvaluationContext(buildContext);
    }
}
