/*
 * Copyright (C) 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmavenplus.model;


/**
 * Container for version information in the form of <tt>major.minor.revision-tag</tt>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @author Keegan Witt
 */
public class Version implements Comparable<Version> {
    public int major;
    public int minor;
    public int revision;
    public String tag;

    /**
     * @param major
     * @param minor
     * @param revision
     * @param tag
     */
    public Version(int major, int minor, int revision, String tag) {
        if (major <= 0 || minor < 0 || revision < 0) {
            // note we don't check the tag since it can be null
            throw new IllegalArgumentException("Major must be > 0 and minor >= 0 and revision >= 0");
        }

        this.major = major;
        this.minor = minor;
        this.revision = revision;
        this.tag = tag;
    }

    /**
     * @param major
     * @param minor
     * @param revision
     */
    public Version(int major, int minor, int revision) {
        this(major, minor, revision, null);
    }

    /**
     * @param major
     * @param minor
     */
    public Version(int major, int minor) {
        this(major, minor, 0);
    }

    /**
     * @param major
     */
    public Version(int major) {
        this(major, 0);
    }

    /**
     * @param version
     * @return
     */
    public static Version parseFromString(String version) {
        if (version == null || version.isEmpty()) {
            throw new IllegalArgumentException("Version must not be null or empty");
        }
        String[] split = version.split("[.-]", 4);
        try {
            int major = Integer.parseInt(split[0]);
            int minor = 0;
            int revision = 0;
            if (split.length >= 2) {
                minor = Integer.parseInt(split[1]);
            }
            if (split.length >= 3) {
                revision= Integer.parseInt(split[2]);
            }
            return new Version(major, minor, revision);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Major, minor, and revision must be integers");
        }
    }

    /**
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Version version = (Version) obj;

        if (major != version.major) {
            return false;
        }
        else if (minor != version.minor) {
            return false;
        }
        else if (revision != version.revision) {
            return false;
        }
        else if (tag != null ? !tag.equals(version.tag) : version.tag != null) {
            return false;
        }

        return true;
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        int result;

        result = major;
        result = 31 * result + minor;
        result = 31 * result + revision;
        result = 31 * result + (tag != null ? tag.hashCode() : 0);

        return result;
    }

    /**
     * @return
     */
    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();

        buff.append(major);

        if (minor != -1) {
            buff.append(".").append(minor);
        }
        if (revision != -1) {
            buff.append(".").append(revision);
        }
        if (tag != null) {
            buff.append("-").append(tag);
        }
        
        return buff.toString();
    }

    /**
     * Compares versions.  Note that if the major, minor, and revision are all
     * the same tags are compared with {@link java.lang.String#compareTo(String) String.compareTo()}
     *
     * @param version
     * @return
     */
    @Override
    public int compareTo(Version version) {
        int mine = (100 * major) + (10 * minor) + revision;
        int theirs = (100 * version.major) + (10 * version.minor) + version.revision;

        if (mine == theirs) {
            return tag.compareTo(version.tag);
        } else {
            return mine - theirs;
        }
    }

}
