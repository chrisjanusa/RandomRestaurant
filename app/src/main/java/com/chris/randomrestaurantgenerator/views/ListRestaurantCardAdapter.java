package com.chris.randomrestaurantgenerator.views;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chris.randomrestaurantgenerator.R;
import com.chris.randomrestaurantgenerator.db.RestaurantDBHelper;
import com.chris.randomrestaurantgenerator.db.DislikeRestaurantDBHelper;
import com.chris.randomrestaurantgenerator.models.Restaurant;
import com.chris.randomrestaurantgenerator.utils.DislikeListHolder;
import com.chris.randomrestaurantgenerator.utils.SavedListHolder;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * A RecyclerView Adapter for the savedList.
 */
public class ListRestaurantCardAdapter extends RecyclerView.Adapter<ListRestaurantCardAdapter.RestaurantViewHolder> {

    private Context context;
    private SavedListHolder savedListHolder;
    private RestaurantDBHelper dbHelper;
    private DislikeListHolder dislikeListHolder;
    private DislikeRestaurantDBHelper dislikedbHelper;




    public ListRestaurantCardAdapter(Context con) {
        this.context = con;
        this.savedListHolder = SavedListHolder.getInstance();
        this.dbHelper = new RestaurantDBHelper(this.context, null);
        this.dislikeListHolder = DislikeListHolder.getInstance();
        this.dislikedbHelper = new DislikeRestaurantDBHelper(this.context, null);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_restaurant_card, parent, false);

        return new RestaurantViewHolder(view);
    }

    public void remove(int index) {
        Restaurant deleteThis = savedListHolder.getSavedList().get(index);
        new DeleteFromDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, deleteThis);
        deleteThis.setSaved(false);
        savedListHolder.getSavedList().remove(deleteThis);
        notifyItemRemoved(index);
    }
    private void addToSavedList(Restaurant res) {
        savedListHolder.getSavedList().add(res);
    }
    private void removeFromSavedList(Restaurant res) {
        savedListHolder.getSavedList().remove(res);
    }
    private void addToDislikeList(Restaurant res) {
        dislikeListHolder.getSavedList().add(res);
    }
    private void removeFromDislikeList(Restaurant res) {
        dislikeListHolder.getSavedList().remove(res);
    }
    public void removeDislike(int index) {
        Restaurant deleteThis = dislikeListHolder.getSavedList().get(index);
        new DeleteFromDislikeDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, deleteThis);
        deleteThis.setSaved(false);
        dislikeListHolder.getSavedList().remove(deleteThis);
        notifyItemRemoved(index);
    }

    public void removeAll() {
        if (savedListHolder.getSavedList() == null) return;

        for (Restaurant r : savedListHolder.getSavedList())
            r.setSaved(false);

        int amount = savedListHolder.getSavedList().size();
        savedListHolder.getSavedList().clear();
        notifyItemRangeRemoved(0, amount);
        new DeleteAllFromDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Helper function to load the corresponding rating image for Yelp stars.
     * Since Yelp v3 API (dubbed Fusion), they stopped providing a URL for the rating image, so this
     * is why this function is needed.
     *
     * @param rating: rating to load stars for.
     * @return corresponding drawable to the rating.
     */
    private int loadStars(Double rating) {
        if (rating == 0.0)
            return R.drawable.ic_zero_stars;
        else if (rating == 1.0)
            return R.drawable.ic_one_stars;
        else if (rating == 1.5)
            return R.drawable.ic_onehalf_stars;
        else if (rating == 2.0)
            return R.drawable.ic_two_stars;
        else if (rating == 2.5)
            return R.drawable.ic_twohalf_stars;
        else if (rating == 3.0)
            return R.drawable.ic_three_stars;
        else if (rating == 3.5)
            return R.drawable.ic_threehalf_stars;
        else if (rating == 4.0)
            return R.drawable.ic_four_stars;
        else if (rating == 4.5)
            return R.drawable.ic_fourhalf_stars;
        else if (rating == 5.0)
            return R.drawable.ic_five_stars;
        else
            return R.drawable.ic_zero_stars;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        savedListHolder.setSavedList(dbHelper.getAll());
        dislikeListHolder.setSavedList(dislikedbHelper.getAll());

        Restaurant restaurant = savedListHolder.getSavedList().get(position);
        holder.index.setText(String.valueOf(position));

        Picasso.with(context).load(restaurant.getThumbnailURL()).into(holder.thumbnail);
        holder.ratingImage.setImageResource(loadStars(restaurant.getRating()));
        holder.nameOfRestaurant.setText(restaurant.getName());
        holder.categories.setText(restaurant.getCategories().toString()
                .replace("[", "").replace("]", "").trim());

        if (restaurant.getDeal().length() != 0) {
            holder.deals.setVisibility(View.VISIBLE);
            holder.deals.setText(restaurant.getDeal());
        } else
            holder.deals.setVisibility(View.GONE);

        String priceText = String.format("%s", restaurant.getPrice());
        String reviewsText = String.format(Locale.ENGLISH, "%d reviews", restaurant.getReviewCount());
        String distanceText = String.format(Locale.ENGLISH, "%.2f mi away", restaurant.getDistance());

        // Color coding dollar signs for price and number of reviews.
        Spannable spannable = new SpannableString(String.format(Locale.ENGLISH, "%s | %s | %s",
                priceText, reviewsText, distanceText));

        int startIndex = 0;
        int endIndex = priceText.length();

        // Price
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#14AD5F")),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        startIndex = endIndex + 3;
        endIndex += 3 + String.valueOf(restaurant.getReviewCount()).length();

        // Review count
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#1A6C9D")),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        startIndex = endIndex + 3 +
                (reviewsText.length() - String.valueOf(restaurant.getReviewCount()).length());
        endIndex = startIndex + (distanceText.length() - " mi away".length());

        // Distance (miles away)
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#FF764A")),
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.distancePriceReviewCount.setText(spannable);

        if (savedListHolder.getSavedList().contains(restaurant)) {
            holder.saveButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_up_filled));
        } else
            holder.saveButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_up_outline));
        if (dislikeListHolder.getSavedList().contains(restaurant)) {
            holder.removeButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_down_filled));
        } else
            holder.removeButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_down_outline));
    }

    @Override
    public int getItemCount() {
        return savedListHolder.getSavedList().size();
    }

    class RestaurantViewHolder extends RecyclerView.ViewHolder {

        TextView nameOfRestaurant;
        ImageView ratingImage;
        ImageView thumbnail;
        TextView categories;
        TextView deals;
        TextView distancePriceReviewCount;
        TextView index;
        ImageButton saveButton;
        ImageButton removeButton;

        RestaurantViewHolder(View itemView) {
            super(itemView);

            nameOfRestaurant = (TextView) itemView.findViewById(R.id.listName);
            index = (TextView) itemView.findViewById(R.id.index);
            final Restaurant restaurant = savedListHolder.getSavedList().get(Integer.parseInt(index.getText().toString()));
            ratingImage = (ImageView) itemView.findViewById(R.id.listRating);
            thumbnail = (ImageView) itemView.findViewById(R.id.listThumbnail);
            categories = (TextView) itemView.findViewById(R.id.listCategories);
            deals = (TextView) itemView.findViewById(R.id.listDeals);
            distancePriceReviewCount = (TextView) itemView.findViewById(R.id.listDistancePriceReviewCount);
            index = (TextView) itemView.findViewById(R.id.index);

            removeButton = (ImageButton) itemView.findViewById(R.id.removeButton);
            saveButton = (ImageButton) itemView.findViewById(R.id.saveButton);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Restaurant r : savedListHolder.getSavedList()) {
                        if (r.equals(restaurant)) {
                            remove(Integer.parseInt(index.getText().toString()));
                            saveButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_up_outline));
                            Toast.makeText(context, "Restaurant removed from like list", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    new InsertIntoDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, restaurant);


                    addToSavedList(restaurant);
                    restaurant.setSaved(true);
                    saveButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_up_filled));
                    Toast.makeText(context, "Restaurant added to like list.", Toast.LENGTH_SHORT).show();
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Restaurant r : dislikeListHolder.getSavedList()) {
                        if (r.equals(restaurant)) {
                            removeDislike(Integer.parseInt(index.getText().toString()));
                            removeButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_down_outline));
                            Toast.makeText(context, "Restaurant removed from dislike list", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    new InsertIntoDislikeDB().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, restaurant);

                    addToDislikeList(restaurant);
                    restaurant.setDislikeSaved(true);
                    removeButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.thumb_down_filled));
                    Toast.makeText(context, "Restaurant added to dislike list", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private class DeleteFromDislikeDB extends AsyncTask<Restaurant, Void, Void> {

        @Override
        protected Void doInBackground(Restaurant... params) {
            dislikedbHelper.delete(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
    private class DeleteFromDB extends AsyncTask<Restaurant, Void, Void> {

        @Override
        protected Void doInBackground(Restaurant... params) {
            dbHelper.delete(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class DeleteAllFromDB extends AsyncTask<Restaurant, Void, Void> {

        @Override
        protected Void doInBackground(Restaurant... params) {
            dbHelper.deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class InsertIntoDB extends AsyncTask<Restaurant, Void, Void> {

        @Override
        protected Void doInBackground(Restaurant... params) {
            dbHelper.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class InsertIntoDislikeDB extends AsyncTask<Restaurant, Void, Void> {

        @Override
        protected Void doInBackground(Restaurant... params) {
            dislikedbHelper.insert(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
