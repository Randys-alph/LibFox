package pepes.co.libfox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/** Shared avatar dropdown: Profile + Log Out (matches Figma Menu-List). */
public final class MenuHelper {

    private static final String PREFS_NAME = "libfox_prefs";

    private MenuHelper() { }

    public static void showProfileMenu(Activity activity, View anchor) {
        View view = LayoutInflater.from(activity).inflate(R.layout.popup_profile, null);

        int width = Math.round(200 * activity.getResources().getDisplayMetrics().density);
        PopupWindow popup = new PopupWindow(view, width,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setElevation(14f);

        view.findViewById(R.id.popup_profile).setOnClickListener(v -> {
            popup.dismiss();
            activity.startActivity(new Intent(activity, ProfileActivity.class));
        });

        view.findViewById(R.id.popup_logout).setOnClickListener(v -> {
            popup.dismiss();
            logout(activity);
        });

        int yOffset = Math.round(8 * activity.getResources().getDisplayMetrics().density);
        popup.showAsDropDown(anchor, 0, yOffset, Gravity.END);
    }

    public static void logout(Activity activity) {
        activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().clear().apply();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
