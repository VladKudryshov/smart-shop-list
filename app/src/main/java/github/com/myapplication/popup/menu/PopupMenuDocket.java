package github.com.myapplication.popup.menu;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import github.com.myapplication.R;
import github.com.myapplication.firebase.helper.DocketCrud;
import github.com.myapplication.model.Docket;

/**
 * Created by Владислав on 04.03.2018.
 */

public class PopupMenuDocket {
    public static void showPopupMenu(Context context, View v, final int position, final Docket model) {
        final PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.list_popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Move: {
                        DocketCrud.moveToBasket(model,position);
                        break;
                    }
                    case R.id.Delete: {
                        DocketCrud.delete(position);
                        break;
                    }

                }

                return false;
            }
        });

        popupMenu.show();
    }
}
