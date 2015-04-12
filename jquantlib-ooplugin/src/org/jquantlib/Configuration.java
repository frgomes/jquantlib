/*
 Copyright (C) 2007 Richard Gomes

 This source code is release under the BSD License.
 
 This file is part of JQuantLib, a free-software/open-source library
 for financial quantitative analysts and developers - http://jquantlib.org/

 JQuantLib is free software: you can redistribute it and/or modify it
 under the terms of the JQuantLib license.  You should have received a
 copy of the license along with this program; if not, please email
 <jquant-devel@lists.sourceforge.net>. The license is also available online at
 <http://www.jquantlib.org/index.php/LICENSE.TXT>.

 This program is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the license for more details.
 
 JQuantLib is based on QuantLib. http://quantlib.org/
 When applicable, the original copyright notice follows this notice.
 */

package org.jquantlib;

import java.util.prefs.Preferences;

/**
 * This provides configuration facility for the application. Provides two types
 * of configurations, System wide configuration and user configuration. System
 * wide configuration is intended to be shared by the whole system and user
 * configurations are for user(or session) specific. To enable user
 * configurations, invoke enableUserConfigurations.
 * <p>
 * Singleton pattern is used for the system wide configuration. In an
 * application server environment, singleton instance could be by class loader
 * depending on scope of the jquantlib library to the module.
 * 
 * <p>
 * Each configuration can have one or more Settings. By default every
 * configuration contains a global Settings that can be shared by multiple
 * computations. Multiple Settings creation can be enabled by calling
 * enableMultipleSettings.
 * 
 * <p>
 * System wide configurations are intended to be constant during the entire life
 * cycle of the application.
 * 
 * @note In heavily multi-threaded environments threads must cache
 *       configurations from this singleton.
 */

//
//TODO: code review :: see bottom of this file for original C++ defines
//
//Some ideas about OSGi
//1. Use interfaces from OSGi and not classes from Sun
//2. Use the default implementation provided by SpringFramework
//
//Some ideas about the API
//1. Create another layer on top of OSGi which provides the configurations we need, hiding the details.
//2. Organize our layers as QuantLib. See config.hpp and userconfig.hpp
//

public class Configuration {

    /**
     * Singleton instance for the whole application. In an application server
     * environment, it could be by class loader depending on scope of the
     * jquantlib library to the module.
     * 
     * @see <a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">The "Double-Checked Locking is Broken" Declaration </a>
     */
    private volatile static Configuration systemConfiguration = null;

    /**
     * Indicates whether user configurations are allowed or not. Default is
     * false which means singleton instance for the whole application.
     */
    private static boolean allowUserConfigurations = false;

    /**
     * Indicates whether a Configuration shares same Settings instance or not.
     * Default is true which means one Settings per Configuration. Value of
     * false will enforce single instance of Setting per Configuration.
     */
    private boolean multipleSettings = false;

    /**
     * Global settings of a Configuration
     */
    private Settings globalSettings = null;

    /**
     * Preferences for the configuration.
     */
    private Preferences preferences = null;

    // TODO: Explain these
    private static boolean defaultExtraSafefyChecks = true;
    private static boolean defaultEnforcesTodaysHistoricFixings = false;
    /**
     * To enforce extra validations
     */
    private boolean extraSafetyChecks;
    /**
     * 
     */
    private boolean enforcesTodaysHistoricFixings;

    /**
     * 
     * @param prefs
     */
    private Configuration(Preferences prefs) {
        this.preferences = prefs;
        if (prefs != null) {
            this.extraSafetyChecks = prefs.getBoolean("ExtraSafetyChecks", defaultExtraSafefyChecks);
            this.enforcesTodaysHistoricFixings = prefs.getBoolean("EnforcesTodaysHistoricFixings",
                    defaultEnforcesTodaysHistoricFixings);
        }
        this.globalSettings = new Settings(prefs);
    }

    /**
     * Returns a System Configuration instance. when called the first time,
     * singleton instance is initialized with the preferences passed in. For
     * subsequent calls, preferences argument is ignored. Configuration returned
     * is a singleton instance, so it can be shared by multiple clients.
     * 
     * @return
     */
    public static Configuration getSystemConfiguration(Preferences prefs) {
        // Double check locking has been fixed in J2SE 5.0 or above with the
        // usage of volatile
        if (systemConfiguration == null) {
            synchronized (Configuration.class) {
                if (systemConfiguration == null) {
                    systemConfiguration = new Configuration(prefs);
                }
            }
        }
        return systemConfiguration;
    }

    /**
     * Returns a new Configuration instance if enableUserConfigurations is set
     * to true. null is returned when allowUserConfigurations is set to false.
     * Its the client responsibility to hold onto the configuration instance for
     * all its usage.
     * 
     * @return
     */
    public static Configuration newConfiguration(Preferences prefs) {
        if (allowUserConfigurations)
            return new Configuration(prefs);
        return null;
    }

    /**
     * Enable multiple user configurations
     */
    public static void enableUserConfigurations() {
        allowUserConfigurations = true;
    }

    /**
     * Returns preferences set on the Configuration.
     * 
     * @return
     */
    public Preferences getPreferences() {
        return this.preferences;
    }

    /**
     * Enables creation of multiple settings per Configuration. If your
     * calculations requires different settings for different calculation, set
     * this to true and call createSettings to create new Settings for each
     * usage.
     */
    public void enableMultipleSettings() {
        this.multipleSettings = true;
    }

    /**
     * Returns the global settings of the Configuration.
     * 
     * @return
     */
    public Settings getGlobalSettings() {
        return globalSettings;
    }

    /**
     * To create a new Settings instance. Returns null if enableMultipleSettings
     * is not set. Settings returned should be cached by the client for its
     * usage.
     * 
     * @return
     */
    public Settings newSettings() {
        if (multipleSettings)
            return new Settings(preferences);
        return null;
    }

    /**
     * Whether to enforce extra checks on the calculations or not.
     * 
     * //TODO: Q ? Should this be at Settings level ?
     * 
     * @return
     */
    public boolean isExtraSafetyChecks() {
        return extraSafetyChecks;
    }

    /**
     * Enforce today's historic fixings
     * 
     * //TODO: Q ? Should this be at Settings level ?
     * 
     * @return
     */
    public boolean isEnforcesTodaysHistoricFixings() {
        return enforcesTodaysHistoricFixings;
    }
}

// ====================================================================================

///* Name of package */
//#define PACKAGE "QuantLib"
//
///* Define to the address where bug reports for this package should be sent. */
//#define PACKAGE_BUGREPORT "quantlib-dev@lists.sourceforge.net"
//
///* Define to the full name of this package. */
//#define PACKAGE_NAME "QuantLib"
//
///* Define to the full name and version of this package. */
//#define PACKAGE_STRING "QuantLib 0.8.1"
//
///* Define to the one symbol short name of this package. */
//#define PACKAGE_TARNAME "QuantLib"
//
///* Define to the version of this package. */
//#define PACKAGE_VERSION "0.8.1"
//
///* Define this if you want to enable sessions. */
///* #undef QL_ENABLE_SESSIONS */
//
///* Define this if tracing messages should allowed (whether they are actually
//   emitted will depend on run-time settings.) */
///* #undef QL_ENABLE_TRACING */
//
///* Define this if extra safety checks should be performed. This can degrade
//   performance. */
///* #undef QL_EXTRA_SAFETY_CHECKS */
//
///* Define this if negative yield rates should be allowed. This might not be
//   safe. */
///* #undef QL_NEGATIVE_RATES */
//
///* Define this if payments occurring today should enter the NPV of an
//   instrument. */
///* #undef QL_TODAYS_PAYMENTS */
//
///* Define this to use indexed coupons instead of par coupons in floating legs.
//   */
///* #undef QL_USE_INDEXED_COUPON */
//
///* Version number of package */
//#define VERSION "0.8.1"
//
//
