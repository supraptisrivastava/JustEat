import RestaurantCard from "./RestaurantCard";
import MenuItemCard from "./MenuItemCard";

const SkeletonCard = () => (
  <div className="flex-shrink-0 w-64 bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md animate-pulse">
    <div className="w-full h-40 bg-gray-200 dark:bg-gray-700" />
    <div className="p-3 space-y-2">
      <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4" />
      <div className="h-3 bg-gray-200 dark:bg-gray-700 rounded w-full" />
      <div className="h-3 bg-gray-200 dark:bg-gray-700 rounded w-1/2" />
    </div>
  </div>
);

const RecommendationSection = ({
  title,
  emoji,
  items = [],
  type = "restaurant",
  loading = false,
  favouriteIds = new Set(),
  onFavouriteToggle,
}) => {
  if (!loading && items.length === 0) return null;

  return (
    <section className="mb-10">
      <h2 className="text-xl font-bold text-gray-900 dark:text-white mb-4 flex items-center gap-2">
        <span>{emoji}</span> {title}
      </h2>
      <div className="flex gap-4 overflow-x-auto pb-2 scrollbar-hide">
        {loading
          ? Array.from({ length: 4 }).map((_, i) => <SkeletonCard key={i} />)
          : items.map((item) =>
              type === "restaurant" ? (
                <RestaurantCard
                  key={item.publicId}
                  restaurant={item}
                  initialFavourited={favouriteIds.has(item.publicId)}
                  onFavouriteToggle={onFavouriteToggle}
                />
              ) : (
                <MenuItemCard
                  key={item.id}
                  item={item}
                  initialFavourited={favouriteIds.has(item.id)}
                  onFavouriteToggle={onFavouriteToggle}
                />
              )
            )}
      </div>
    </section>
  );
};

export default RecommendationSection;

