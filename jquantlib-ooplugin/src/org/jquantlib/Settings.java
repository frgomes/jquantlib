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

import org.jquantlib.lang.annotation.PackagePrivate;
import org.jquantlib.time.Date;

/**
 * Settings for the application.
 * 
 * <p>
 * Settings are mutable values which have a life cycle of a certain operation of
 * sequence of operations defined by the application.
 * 
 */

//
//TODO: code review :: see bottom of this file for original C++ defines
//
// Some ideas about OSGi
// 1. Use interfaces from OSGi and not classes from Sun
// 2. Use the default implementation provided by SpringFramework
//
// Some ideas about the API
// 1. Create another layer on top of OSGi which provides the configurations we need, hiding the details.
// 2. Organize our layers as QuantLib. See config.hpp and userconfig.hpp
//

public class Settings {

    private static boolean defaultTodaysPayments = false;

    /**
     * This field determines whether payments expected to happen at the
     * <i>current evaluation date</i> are considered.
     * 
     * @see #isTodaysPayments()
     */
    private boolean todaysPayments;

    /**
     * Define this if negative yield rates should be allowed. This might not be safe.
     * 
     * @see #isNegativeRates
     */
    private boolean negativeRates;

    /**
     * This field keeps the current evaluation date.
     * 
     * <p>
     * Notice that the current evaluation date <b>is not necessarily</b> the
     * current date or today's date in other words. In the specific situation
     * when the evaluation date is never defined explicitly, then today's date
     * is assume by default.
     */
    private Date evaluationDate;

    /**
     * Default constructor
     * 
     * @see defaultExtraSafefyChecks
     * @see defaultEnforcesTodaysHistoricFixings
     * @see defaultTodaysPayments
     */
    @PackagePrivate Settings() {
        setDefaults();
    }

    /**
     * This constructor is provided so that application settings can be
     * initialized via implementation independent Preferences.
     * 
     * @param prefs
     *            is a Preferences object
     */
    @PackagePrivate Settings(final Preferences prefs) {
        if (prefs != null) {
            this.todaysPayments = prefs.getBoolean("TodaysPayments", defaultTodaysPayments);
            this.evaluationDate = Date.todaysDate();
        } else {
            setDefaults();
        }
    }

    private void setDefaults() {
        this.todaysPayments = defaultTodaysPayments;
        this.evaluationDate = Date.todaysDate();
    }

    /**
     * @return the value of field todaysPayments
     * 
     * @see #todaysPayments
     */
    public boolean isTodaysPayments() {
        return todaysPayments;
    }

    /**
     * @return the value of field negativeRates
     * 
     * @see #{@link #negativeRates}
     */
    public boolean isNegativeRates() {
        return negativeRates;
    }

    /**
     * @return the value of field evaluationDate
     * 
     * @see #evaluationDate
     */
    public Date getEvaluationDate() {
        return evaluationDate;
    }

    /**
     * Changes the value of field todaysPayments
     * 
     * @see #todaysPayments
     */
    public void setTodaysPayments(final boolean todaysPayments) {
        this.todaysPayments = todaysPayments;
    }

    /**
     * Changes the value of field evaluationDate.
     * 
     * <p>
     * Notice that a successful change of evaluationDate notifies all its
     * listeners.
     * 
     * @see #evaluationDate
     */
    public void setEvaluationDate(final Date evaluationDate) {
        this.setEvaluationDate(evaluationDate);
    }

}




/***************************************************************
User configuration section:
modify the following definitions to suit your preferences.

Do not modify this file if you are using a Linux/Unix system:
it will not be read by the compiler. The definitions below
will be provided by running ./configure instead.
****************************************************************/

///* Define this if error messages should include current function
//information. */
//#ifndef QL_ERROR_FUNCTIONS
////#   define QL_ERROR_FUNCTIONS
//#endif
//
///* Define this if error messages should include file and line information. */
//#ifndef QL_ERROR_LINES
////#   define QL_ERROR_LINES
//#endif
//
///* Define this if tracing messages should be allowed (whether they are
//actually emitted will depend on run-time settings.) */
//#ifndef QL_ENABLE_TRACING
////#   define QL_ENABLE_TRACING
//#endif
//
///* Define this if negative yield rates should be allowed. This might not be
//safe. */
//#ifndef QL_NEGATIVE_RATES
////#   define QL_NEGATIVE_RATES
//#endif
//
///* Define this if extra safety checks should be performed. This can degrade
//performance. */
//#ifndef QL_EXTRA_SAFETY_CHECKS
////#   define QL_EXTRA_SAFETY_CHECKS
//#endif
//
///* Define this if payments occurring today should enter the NPV of an
//instrument. */
//#ifndef QL_TODAYS_PAYMENTS
////#   define QL_TODAYS_PAYMENTS
//#endif
//
///* Define this if you want to disable deprecated code. */
//#ifndef QL_DISABLE_DEPRECATED
////#   define QL_DISABLE_DEPRECATED
//#endif
//
///* Define this to use indexed coupons instead of par coupons in floating
//legs. */
//#ifndef QL_USE_INDEXED_COUPON
////#   define QL_USE_INDEXED_COUPON
//#endif
//
///* Define this to have singletons return different instances for
//different sessions. You will have to provide and link with the
//library a sessionId() function in namespace QuantLib, returning a
//different session id for each session.*/
//#ifndef QL_ENABLE_SESSIONS
////#   define QL_ENABLE_SESSIONS
//#endif


