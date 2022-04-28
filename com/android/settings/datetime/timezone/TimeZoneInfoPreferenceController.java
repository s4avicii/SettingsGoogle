package com.android.settings.datetime.timezone;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import androidx.window.C0444R;
import com.android.settings.core.BasePreferenceController;
import java.time.Instant;
import java.time.zone.ZoneOffsetTransition;
import java.time.zone.ZoneRules;
import java.util.Date;

public class TimeZoneInfoPreferenceController extends BasePreferenceController {
    Date mDate = new Date();
    private final DateFormat mDateFormat;
    private TimeZoneInfo mTimeZoneInfo;

    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TimeZoneInfoPreferenceController(Context context, String str) {
        super(context, str);
        DateFormat dateInstance = DateFormat.getDateInstance(1);
        this.mDateFormat = dateInstance;
        dateInstance.setContext(DisplayContext.CAPITALIZATION_NONE);
    }

    public int getAvailabilityStatus() {
        return this.mTimeZoneInfo != null ? 1 : 3;
    }

    public CharSequence getSummary() {
        TimeZoneInfo timeZoneInfo = this.mTimeZoneInfo;
        return timeZoneInfo == null ? "" : formatInfo(timeZoneInfo);
    }

    public void setTimeZoneInfo(TimeZoneInfo timeZoneInfo) {
        this.mTimeZoneInfo = timeZoneInfo;
    }

    private CharSequence formatOffsetAndName(TimeZoneInfo timeZoneInfo) {
        String genericName = timeZoneInfo.getGenericName();
        if (genericName == null) {
            if (timeZoneInfo.getTimeZone().inDaylightTime(this.mDate)) {
                genericName = timeZoneInfo.getDaylightName();
            } else {
                genericName = timeZoneInfo.getStandardName();
            }
        }
        if (genericName == null) {
            return timeZoneInfo.getGmtOffset().toString();
        }
        return SpannableUtil.getResourcesText(this.mContext.getResources(), C0444R.string.zone_info_offset_and_name, timeZoneInfo.getGmtOffset(), genericName);
    }

    private CharSequence formatInfo(TimeZoneInfo timeZoneInfo) {
        CharSequence formatOffsetAndName = formatOffsetAndName(timeZoneInfo);
        TimeZone timeZone = timeZoneInfo.getTimeZone();
        if (!timeZone.observesDaylightTime()) {
            return this.mContext.getString(C0444R.string.zone_info_footer_no_dst, new Object[]{formatOffsetAndName});
        }
        ZoneOffsetTransition findNextDstTransition = findNextDstTransition(timeZoneInfo);
        if (findNextDstTransition == null) {
            return this.mContext.getString(C0444R.string.zone_info_footer_no_dst, new Object[]{formatOffsetAndName});
        }
        boolean z = getDSTSavings(timeZone, findNextDstTransition.getInstant()) != 0;
        String daylightName = z ? timeZoneInfo.getDaylightName() : timeZoneInfo.getStandardName();
        if (daylightName == null) {
            if (z) {
                daylightName = this.mContext.getString(C0444R.string.zone_time_type_dst);
            } else {
                daylightName = this.mContext.getString(C0444R.string.zone_time_type_standard);
            }
        }
        Calendar instance = Calendar.getInstance(timeZone);
        instance.setTimeInMillis(findNextDstTransition.getInstant().toEpochMilli());
        return SpannableUtil.getResourcesText(this.mContext.getResources(), C0444R.string.zone_info_footer, formatOffsetAndName, daylightName, this.mDateFormat.format(instance));
    }

    private ZoneOffsetTransition findNextDstTransition(TimeZoneInfo timeZoneInfo) {
        ZoneOffsetTransition nextTransition;
        TimeZone timeZone = timeZoneInfo.getTimeZone();
        ZoneRules rules = timeZoneInfo.getJavaTimeZone().toZoneId().getRules();
        Instant instant = this.mDate.toInstant();
        while (true) {
            nextTransition = rules.nextTransition(instant);
            if (nextTransition == null) {
                break;
            }
            Instant instant2 = nextTransition.getInstant();
            if (getDSTSavings(timeZone, instant) != getDSTSavings(timeZone, instant2)) {
                break;
            }
            instant = instant2;
        }
        return nextTransition;
    }

    private static int getDSTSavings(TimeZone timeZone, Instant instant) {
        int[] iArr = new int[2];
        timeZone.getOffset(instant.toEpochMilli(), false, iArr);
        return iArr[1];
    }
}
