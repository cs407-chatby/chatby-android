package io.github.cs407_chatby.chatby.ui.main.active;


public abstract class ItemSource extends DataSource {

    @Override
    public int getItemCount() {
        return 1;
    }

    class Builder {
        OnGetItemViewType viewTypeCallback = () -> 0;
        OnGetItem itemCallback = () -> null;

        public Builder setViewType(OnGetItemViewType callback) {
            viewTypeCallback = callback;
            return this;
        }

        public Builder setItem(OnGetItem callback) {
            itemCallback = callback;
            return this;
        }

        public ItemSource build() {
            return new ItemSource() {
                @Override
                public int getItemViewType(int position) {
                    return viewTypeCallback.getViewType();
                }

                @Override
                public Object getItem(int position) {
                    return itemCallback.getItem();
                }
            };
        }
    }

    interface OnGetItemViewType {
        int getViewType();
    }

    interface OnGetItem {
        Object getItem();
    }
}
