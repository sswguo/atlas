/*******************************************************************************
 * Copyright (C) 2014 John Casey.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.commonjava.maven.atlas.graph.filter;

import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;
import org.commonjava.maven.atlas.graph.rel.RelationshipType;

public class BomFilter
    extends AbstractTypedFilter
{

    private static final long serialVersionUID = 1L;

    public static final BomFilter INSTANCE = new BomFilter();

    private BomFilter()
    {
        // BOMs are actually marked as concrete...somewhat counter-intuitive, 
        // but they're structural, so managed isn't quite correct (despite 
        // Maven's unfortunate choice for location).
        super( RelationshipType.BOM, true, false, true );
    }

    @Override
    public boolean doAccept( final ProjectRelationship<?> rel )
    {
        return true;
    }

    @Override
    public ProjectRelationshipFilter getChildFilter( final ProjectRelationship<?> parent )
    {
        return this;
    }

}