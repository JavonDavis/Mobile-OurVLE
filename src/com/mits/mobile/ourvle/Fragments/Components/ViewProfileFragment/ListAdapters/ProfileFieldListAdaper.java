package com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ListAdapters;

import java.util.List;

import org.sourceforge.ah.android.utilities.Formatters.TextFormatter;
import org.sourceforge.ah.android.utilities.Widgets.Adapters.PinnedHeaderListAdapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.UserProfileField;
import com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ButtonListeners.EmailButtonListener;
import com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ButtonListeners.PhoneButtonListener;
import com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.ButtonListeners.WebsiteButtonListener;
import com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.Constants.UserProfileFieldCategories;
import com.mits.mobile.ourvle.Fragments.Components.ViewProfileFragment.Constants.UserProfileFieldCategories.Category;

public class ProfileFieldListAdaper extends
	PinnedHeaderListAdapter<String, UserProfileField> {

    private final Context mContext;

    private class ViewHolder implements SimpleViewHolder {
	private TextView fieldValue;
	private TextView fieldType;
	private ImageButton fieldButton;
	private ImageView divider;
	private ImageView fieldIcon;
    }

    public ProfileFieldListAdaper(final Context context) {
	super(context, PinnedHeaderListAdapter.DEFAULT_HEADER_VIEW,
		R.layout.list_item_profile_field);

	mContext = context;
    }

    @Override
    public int getItemViewTypeCount() {
	return 3;
    }

    @Override
    protected int getItemViewType(final int partition, final int position) {
	switch (getPartitonCategory(partition)) {
	case SOCIAL:
	case OTHER:
	case INFO:
	case PERFERENCES:
	    if (!"url".equals(getItem(position).getRawName()))
		return 1;
	default:
	    return 2;
	}
    }

    private Category getPartitonCategory(final int partition) {
	final Partition<String, UserProfileField> p = getPartition(partition);
	return UserProfileFieldCategories.getPartitionCategory(p);
    }

    @Override
    protected SimpleViewHolder getRowViewHolder(
	    final View rowView) {
	final ViewHolder h = new ViewHolder();
	h.fieldValue = (TextView) rowView
		.findViewById(R.id.textview_field_value);
	h.fieldButton = (ImageButton) rowView
		.findViewById(R.id.imagebutton_field_btn);

	h.fieldType = (TextView) rowView
		.findViewById(R.id.textview_field_type);

	h.divider = (ImageView) rowView
		.findViewById(R.id.imageview_vertical_divider);

	h.fieldIcon = (ImageView) rowView
		.findViewById(R.id.imageview_field_icon);
	return h;
    }

    @Override
    protected View newView(final Context context, final int partition,
	    final List<UserProfileField> dataSource,
	    final int position, final ViewGroup parent) {
	final View v;
	switch (getPartitonCategory(partition)) {
	case SOCIAL:
	case OTHER:
	case PERFERENCES:
	case INFO:
	    if (!"url".equals(dataSource.get(position).getRawName())) {
		v = getLayoutInflater().inflate(
			R.layout.list_item_info_profile_field, parent, false);
		break;
	    }
	default:
	    v = getLayoutInflater().inflate(
		    R.layout.list_item_profile_field, parent, false);
	    break;
	}
	v.setTag(getRowViewHolder(v));
	return v;
    }

    @Override
    protected void bindView(final View v, final int partition,
	    final List<UserProfileField> dataSource, final int position) {
	final UserProfileField rowData = dataSource.get(position);
	final ViewHolder h = (ViewHolder) v.getTag();

	h.fieldValue.setText(TextFormatter.trimSpannedText(
		Html.fromHtml(rowData.getValue())));

	if (h.fieldButton != null)
	    switch (getPartitonCategory(partition)) {
	    case PHONE:
		h.divider.setVisibility(View.VISIBLE);
		h.fieldButton.setVisibility(View.VISIBLE);
		h.fieldButton.setImageResource(R.drawable.ic_phone_call);
		h.fieldButton.setOnClickListener(
			new PhoneButtonListener(mContext, rowData));
		break;
	    case EMAIL:
		h.divider.setVisibility(View.VISIBLE);
		h.fieldButton.setVisibility(View.VISIBLE);
		h.fieldButton.setImageResource(R.drawable.ic_mail);
		h.fieldButton.setOnClickListener(
			new EmailButtonListener(mContext, rowData));
		break;
	    default:
		if (!"url".equals(dataSource.get(position).getRawName())) {
		    h.fieldButton.setVisibility(View.INVISIBLE);
		    h.divider.setVisibility(View.INVISIBLE);
		} else {
		    h.divider.setVisibility(View.VISIBLE);
		    h.fieldButton.setVisibility(View.VISIBLE);
		    h.fieldButton.setImageResource(R.drawable.ic_website);
		    h.fieldButton.setOnClickListener(
			    new WebsiteButtonListener(mContext, rowData));
		}
	    }

	if (h.fieldIcon != null)
	    h.fieldIcon.setImageResource(UserProfileFieldCategories
		    .getFieldIconResoource(rowData.getRawName()));

	if (h.fieldType != null)
	    h.fieldType.setText(rowData.getCleanedName());
    }
}