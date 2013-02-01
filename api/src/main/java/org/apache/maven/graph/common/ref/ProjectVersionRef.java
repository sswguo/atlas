/*******************************************************************************
 * Copyright 2012 John Casey
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.apache.maven.graph.common.ref;

import java.io.Serializable;

import org.apache.maven.graph.common.version.InvalidVersionSpecificationException;
import org.apache.maven.graph.common.version.SingleVersion;
import org.apache.maven.graph.common.version.VersionSpec;
import org.apache.maven.graph.common.version.VersionUtils;

public class ProjectVersionRef
    extends ProjectRef
    implements VersionedRef<ProjectVersionRef>, Serializable
{

    private static final long serialVersionUID = 1L;

    // NEVER null
    private VersionSpec versionSpec;

    private String versionString;

    public ProjectVersionRef( final ProjectRef ref, final VersionSpec versionSpec )
    {
        this( ref.getGroupId(), ref.getArtifactId(), versionSpec, null );
    }

    public ProjectVersionRef( final ProjectRef ref, final String versionSpec )
        throws InvalidVersionSpecificationException
    {
        this( ref.getGroupId(), ref.getArtifactId(), versionSpec );
    }

    ProjectVersionRef( final String groupId, final String artifactId, final VersionSpec versionSpec,
                       final String versionString )
    {
        super( groupId, artifactId );
        if ( versionSpec == null && versionString == null )
        {
            throw new NullPointerException( "Version spec AND string cannot both be null for '" + groupId + ":"
                + artifactId + "'" );
        }

        this.versionString = versionString;
        this.versionSpec = versionSpec;
    }

    public ProjectVersionRef( final String groupId, final String artifactId, final VersionSpec versionSpec )
    {
        this( groupId, artifactId, versionSpec, null );
    }

    public ProjectVersionRef( final String groupId, final String artifactId, final String versionString )
        throws InvalidVersionSpecificationException
    {
        this( groupId, artifactId, null, versionString );
    }

    public ProjectVersionRef asProjectVersionRef()
    {
        return getClass().equals( ProjectVersionRef.class ) ? this : new ProjectVersionRef( getGroupId(),
                                                                                            getArtifactId(),
                                                                                            getVersionSpecRaw(),
                                                                                            getVersionStringRaw() );
    }

    VersionSpec getVersionSpecRaw()
    {
        return versionSpec;
    }

    String getVersionStringRaw()
    {
        return versionString;
    }

    public ProjectRef asProjectRef()
    {
        return new ProjectRef( getGroupId(), getArtifactId() );
    }

    public boolean isRelease()
    {
        return getVersionSpec().isConcrete();
    }

    public boolean isSpecificVersion()
    {
        return getVersionSpec().isSingle();
    }

    public boolean matchesVersion( final SingleVersion version )
    {
        return getVersionSpec().contains( version );
    }

    public ProjectVersionRef selectVersion( final SingleVersion version )
    {
        final VersionSpec versionSpec = getVersionSpec();
        if ( versionSpec.equals( version ) )
        {
            return this;
        }

        if ( !versionSpec.contains( version ) )
        {
            throw new IllegalArgumentException( "Specified version: " + version.renderStandard()
                + " is not contained in spec: " + versionSpec.renderStandard() );
        }

        return newRef( getGroupId(), getArtifactId(), version );
    }

    protected ProjectVersionRef newRef( final String groupId, final String artifactId, final SingleVersion version )
    {
        return new ProjectVersionRef( groupId, artifactId, version );
    }

    public synchronized VersionSpec getVersionSpec()
    {
        if ( versionSpec == null )
        {
            versionSpec = VersionUtils.createFromSpec( versionString );
        }
        return versionSpec;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( getVersionString() == null ) ? 0 : getVersionString().hashCode() );
        return result;
    }

    public boolean versionlessEquals( final ProjectVersionRef other )
    {
        if ( this == other )
        {
            return true;
        }

        return super.equals( other );
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( !super.equals( obj ) )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final ProjectVersionRef other = (ProjectVersionRef) obj;
        if ( getVersionSpec() == null )
        {
            if ( other.getVersionSpec() != null )
            {
                return false;
            }
        }
        else if ( !getVersionSpec().equals( other.getVersionSpec() ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format( "%s:%s:%s", getGroupId(), getArtifactId(), getVersionString() );
    }

    public boolean isCompound()
    {
        return !getVersionSpec().isSingle();
    }

    public boolean isSnapshot()
    {
        return !isCompound() && !isRelease();
    }

    public synchronized String getVersionString()
    {
        if ( versionString == null )
        {
            versionString = versionSpec.renderStandard();
        }

        return versionString;
    }

}