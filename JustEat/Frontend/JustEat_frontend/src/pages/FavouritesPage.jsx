import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import RestaurantCard from "../components/RestaurantCard";
import MenuItemCard from "../components/MenuItemCard";
import { getFavouriteRestaurants, getFavouriteMenuItems } from "../api/favouritesApi";

const SkeletonRow = () => (
  <div className="flex gap-4 overflow-x-auto pb-2">
    {Array.from({ length: 4 }).map((_, i) => (
      <div key={i} className="flex-shrink-0 w-64 bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md animate-pulse">
        <div className="w-full h-40 bg-gray-200 dark:bg-gray-700" />
        <div className="p-3 space-y-2">
          <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4" />
          <div className="h-3 bg-gray-200 dark:bg-gray-700 rounded" />
        </div>
      </div>
    ))}
  </div>
);

const FavouritesPage = () => {
  const [favRestaurants, setFavRestaurants] = useState([]);
  const [favMenuItems, setFavMenuItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState("restaurants");

  useEffect(() => {
    setLoading(true);
    Promise.all([
      getFavouriteRestaurants().catch(() => ({ data: [] })),
      getFavouriteMenuItems().catch(() => ({ data: [] })),
    ]).then(([rRes, mRes]) => {
      setFavRestaurants(rRes.data || []);
      setFavMenuItems(mRes.data || []);
    }).finally(() => setLoading(false));
  }, []);

  const handleRestaurantUnfav = (publicId, isFav) => {
    if (!isFav) setFavRestaurants((prev) => prev.filter((r) => r.publicId !== publicId));
  };

  const handleMenuItemUnfav = (id, isFav) => {
    if (!isFav) setFavMenuItems((prev) => prev.filter((m) => m.id !== id));
  };

  const favRestaurantIds = new Set(favRestaurants.map((r) => r.publicId));
  const favMenuItemIds = new Set(favMenuItems.map((m) => m.id));

  return (
    <>
      <Navbar />
      <div className="px-8 py-8 max-w-7xl mx-auto">
        <div className="mb-8">
          <h1 className="text-3xl font-extrabold text-gray-900 dark:text-white mb-1">❤️ My Favourites</h1>
          <p className="text-gray-500 dark:text-gray-400">Your saved restaurants and dishes</p>
        </div>

        {/* Tabs */}
        <div className="flex gap-3 mb-8 border-b border-gray-200 dark:border-gray-700">
          {[
            { key: "restaurants", label: `🏪 Restaurants (${favRestaurants.length})` },
            { key: "dishes", label: `🍽️ Dishes (${favMenuItems.length})` },
          ].map((t) => (
            <button
              key={t.key}
              onClick={() => setTab(t.key)}
              className={`pb-3 px-2 font-semibold text-sm border-b-2 transition-all cursor-pointer ${
                tab === t.key
                  ? "border-orange-500 text-orange-500"
                  : "border-transparent text-gray-500 dark:text-gray-400 hover:text-orange-500"
              }`}
            >
              {t.label}
            </button>
          ))}
        </div>

        {loading && <SkeletonRow />}

        {!loading && tab === "restaurants" && (
          <>
            {favRestaurants.length === 0 ? (
              <div className="text-center py-20">
                <div className="text-6xl mb-4">🏪</div>
                <h3 className="text-xl font-semibold text-gray-700 dark:text-gray-300 mb-2">No favourite restaurants yet</h3>
                <p className="text-gray-500 dark:text-gray-400 mb-6">Tap the heart icon on any restaurant to save it here.</p>
                <Link to="/" className="bg-orange-500 text-white font-semibold px-6 py-2.5 rounded-xl hover:bg-orange-600 transition-all no-underline">
                  Browse Restaurants
                </Link>
              </div>
            ) : (
              <div className="grid gap-6" style={{ gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))" }}>
                {favRestaurants.map((r) => (
                  <RestaurantCard
                    key={r.publicId}
                    restaurant={r}
                    initialFavourited={favRestaurantIds.has(r.publicId)}
                    onFavouriteToggle={handleRestaurantUnfav}
                  />
                ))}
              </div>
            )}
          </>
        )}

        {!loading && tab === "dishes" && (
          <>
            {favMenuItems.length === 0 ? (
              <div className="text-center py-20">
                <div className="text-6xl mb-4">🍽️</div>
                <h3 className="text-xl font-semibold text-gray-700 dark:text-gray-300 mb-2">No favourite dishes yet</h3>
                <p className="text-gray-500 dark:text-gray-400 mb-6">Tap the heart icon on any dish to save it here.</p>
                <Link to="/" className="bg-orange-500 text-white font-semibold px-6 py-2.5 rounded-xl hover:bg-orange-600 transition-all no-underline">
                  Browse Restaurants
                </Link>
              </div>
            ) : (
              <div className="grid gap-4" style={{ gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))" }}>
                {favMenuItems.map((item) => (
                  <MenuItemCard
                    key={item.id}
                    item={item}
                    initialFavourited={favMenuItemIds.has(item.id)}
                    onFavouriteToggle={handleMenuItemUnfav}
                  />
                ))}
              </div>
            )}
          </>
        )}
      </div>
    </>
  );
};

export default FavouritesPage;

