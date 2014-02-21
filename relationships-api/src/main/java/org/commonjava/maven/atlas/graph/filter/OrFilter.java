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

import java.util.Collection;
import java.util.List;

import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;

public class OrFilter
    extends AbstractAggregatingFilter
{

    //    private final Logger logger = new Logger( getClass() );

    public OrFilter( final Collection<? extends ProjectRelationshipFilter> filters )
    {
        super( filters );
    }

    public <T extends ProjectRelationshipFilter> OrFilter( final T... filters )
    {
        super( filters );
    }

    @Override
    public boolean accept( final ProjectRelationship<?> rel )
    {
        boolean accepted = false;
        for ( final ProjectRelationshipFilter filter : getFilters() )
        {
            accepted = accepted || filter.accept( rel );
            if ( accepted )
            {
                //                logger.info( "ACCEPTed: %s, by sub-filter: %s", rel, filter );
                break;
            }
        }

        return accepted;
    }

    @Override
    protected AbstractAggregatingFilter newChildFilter( final List<ProjectRelationshipFilter> childFilters )
    {
        // TODO: Optimize to minimize new instance creation...
        return new OrFilter( childFilters );
    }

    @Override
    public void render( final StringBuilder sb )
    {
        final List<? extends ProjectRelationshipFilter> filters = getFilters();
        if ( sb.length() > 0 )
        {
            sb.append( " " );
        }
        sb.append( "[" );
        boolean first = true;
        for ( final ProjectRelationshipFilter filter : filters )
        {
            if ( first )
            {
                first = false;
            }
            else
            {
                sb.append( " || " );
            }

            filter.render( sb );
        }
        sb.append( "]" );
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder();
        render( sb );
        return sb.toString();
    }

}
