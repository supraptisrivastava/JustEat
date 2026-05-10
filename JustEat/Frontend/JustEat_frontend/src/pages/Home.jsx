import { useEffect, useState, useCallback } from "react";
import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";
import SearchBar from "../components/SearchBar";
import RecommendationSection from "../components/RecommendationSection";
import PreferencesModal from "../components/PreferencesModal";
import RestaurantCard from "../components/RestaurantCard";
import {
  getRestaurants,
  searchRestaurants,
  getRecommendations,
  getGlobalPopularItems,
} from "../api/restaurantApi";
import { getFavouriteRestaurants, getFavouriteMenuItems } from "../api/favouritesApi";
import { getPreferences } from "../api/preferencesApi";
import { useAuth } from "../context/AuthContext";

const LOCATIONS = ["ALL", "NOIDA", "DELHI", "GURGAON"];

const SkeletonGrid = () => (
  <div className="grid gap-6" style={{ gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))" }}>
    {Array.from({ length: 6 }).map((_, i) => (
      <div key={i} className="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md animate-pulse">
        <div className="w-full h-44 bg-gray-200 dark:bg-gray-700" />
        <div className="p-4 space-y-2">
          <div className="h-4 bg-gray-200 dark:bg-gray-700 rounded w-3/4" />
          <div className="h-3 bg-gray-200 dark:bg-gray-700 rounded" />
          <div className="h-3 bg-gray-200 dark:bg-gray-700 rounded w-1/2" />
        </div>
      </div>
    ))}
  </div>
);

const Home = () => {
  const { userLocation, role } = useAuth();
  const [restaurants, setRestaurants] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [popularItems, setPopularItems] = useState([]);
  const [favRestaurants, setFavRestaurants] = useState([]);
  const [favMenuItems, setFavMenuItems] = useState([]);
  const [preferences, setPreferences] = useState(null);
  const [location, setLocation] = useState(userLocation || "ALL");
  const [loading, setLoading] = useState(true);
  const [recLoading, setRecLoading] = useState(true);
  const [error, setError] = useState("");
  const [isSearching, setIsSearching] = useState(false);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [showPrefsModal, setShowPrefsModal] = useState(false);

  const isCustomer = role === "CUSTOMER";

  useEffect(() => {
    if (!isCustomer) { setRecLoading(false); return; }
    setRecLoading(true);
    Promise.all([
      getRecommendations().catch(() => ({ data: [] })),
      getGlobalPopularItems().catch(() => ({ data: [] })),
      getFavouriteRestaurants().catch(() => ({ data: [] })),
      getFavouriteMenuItems().catch(() => ({ data: [] })),
      getPreferences().catch(() => ({ data: null })),
    ]).then(([recRes, popRes, favRRes, favMRes, prefRes]) => {
      setRecommendations(recRes.data || []);
      setPopularItems(popRes.data || []);
      setFavRestaurants(favRRes.data || []);
      setFavMenuItems(favMRes.data || []);
      setPreferences(prefRes.data || null);
    }).finally(() => setRecLoading(false));
  }, [isCustomer]);

  useEffect(() => {
    if (!isSearching) {
      setLoading(true);
      setError("");
      getRestaurants(location === "ALL" ? null : location)
        .then((res) => setRestaurants(res.data))
        .catch(() => setError("Failed to load restaurants."))
        .finally(() => setLoading(false));
    }
  }, [location, isSearching]);

  const handleSearch = useCallback((searchParams) => {
    if (!searchParams) { setIsSearching(false); setSearchKeyword(""); return; }
    setIsSearching(true);
    setLoading(true);
    setError("");
    setSearchKeyword(searchParams.keyword || "");
    searchRestaurants(searchParams)
      .then((res) => setRestaurants(res.data))
      .catch(() => setError("Failed to search restaurants."))
      .finally(() => setLoading(false));
  }, []);

  const favRestaurantIds = new Set(favRestaurants.map((r) => r.publicId));
  const favMenuItemIds = new Set(favMenuItems.map((m) => m.id));

  const handleRestaurantFavouriteToggle = (publicId, isFav) => {
    if (isFav) {
      const r = [...recommendations, ...restaurants].find((x) => x.publicId === publicId);
      if (r) setFavRestaurants((prev) => [...prev.filter((x) => x.publicId !== publicId), r]);
    } else {
      setFavRestaurants((prev) => prev.filter((x) => x.publicId !== publicId));
    }
  };

  const handlePreferencesSaved = (prefs) => {
    setPreferences(prefs);
    setRecLoading(true);
    getRecommendations().then((res) => setRecommendations(res.data || [])).finally(() => setRecLoading(false));
  };

  return (
    <>
      <Navbar />
      <div className="px-8 py-8 max-w-7xl mx-auto">
        {isCustomer && !isSearching && (
          <div className="mb-8 rounded-2xl bg-gradient-to-r from-orange-500 to-orange-600 text-white px-8 py-10 flex items-center justify-between shadow-lg">
            <div>
              <h1 className="text-3xl font-extrabold mb-2">What are you craving today? 🍕</h1>
              <p className="text-orange-100 text-base">Personalised picks just for you</p>
            </div>
            <button onClick={() => setShowPrefsModal(true)}
              className="bg-white text-orange-500 font-bold px-5 py-2.5 rounded-xl hover:bg-orange-50 transition-all cursor-pointer shadow">
              ⚙️ Preferences
            </button>
          </div>
        )}

        {!isCustomer && !isSearching && (
          <h1 className="text-2xl font-bold mb-6 text-gray-900 dark:text-white">Restaurants near you</h1>
        )}

        {showPrefsModal && (
          <PreferencesModal initial={preferences || {}} onClose={() => setShowPrefsModal(false)} onSaved={handlePreferencesSaved} />
        )}

        <SearchBar onSearch={handleSearch} initialLocation={userLocation || ""} />

        {isCustomer && !isSearching && (
          <>
            <RecommendationSection title="Your Saved Restaurants" emoji="❤️" items={favRestaurants} type="restaurant"
              loading={recLoading} favouriteIds={favRestaurantIds} onFavouriteToggle={handleRestaurantFavouriteToggle} />
            <RecommendationSection title="Recommended For You" emoji="✨" items={recommendations} type="restaurant"
              loading={recLoading} favouriteIds={favRestaurantIds} onFavouriteToggle={handleRestaurantFavouriteToggle} />
            <RecommendationSection title="Popular Dishes Right Now" emoji="🔥" items={popularItems} type="menuitem"
              loading={recLoading} favouriteIds={favMenuItemIds} />
            {!recLoading && recommendations.length === 0 && favRestaurants.length === 0 && (
              <div className="mb-8 p-6 rounded-2xl border-2 border-dashed border-orange-200 dark:border-orange-700 bg-orange-50 dark:bg-orange-900/10 text-center">
                <div className="text-4xl mb-3">🌟</div>
                <h3 className="font-bold text-lg text-gray-800 dark:text-white mb-1">Welcome to JustEat!</h3>
                <p className="text-gray-500 dark:text-gray-400 text-sm mb-4">Start ordering to get personalised recommendations, or set your preferences.</p>
                <button onClick={() => setShowPrefsModal(true)}
                  className="bg-orange-500 text-white font-semibold px-5 py-2 rounded-xl hover:bg-orange-600 transition-all cursor-pointer">
                  Set My Preferences
                </button>
              </div>
            )}
            <div className="border-t border-gray-200 dark:border-gray-700 my-8" />
          </>
        )}

        {!isSearching && (
          <div className="flex gap-3 flex-wrap mb-6 items-center">
            <span className="text-sm font-semibold text-gray-500 dark:text-gray-400">
              {isCustomer ? "Explore All Restaurants:" : "Filter by city:"}
            </span>
            {LOCATIONS.map((loc) => (
              <button key={loc} onClick={() => setLocation(loc)}
                className={`border-2 font-semibold text-sm px-4 py-1.5 rounded-full cursor-pointer transition-all ${
                  location === loc
                    ? "bg-orange-500 border-orange-500 text-white"
                    : "bg-white dark:bg-gray-800 border-gray-200 dark:border-gray-600 text-gray-500 dark:text-gray-400 hover:border-orange-500 hover:text-orange-500"}`}>
                {loc}
              </button>
            ))}
          </div>
        )}

        {isSearching && !loading && (
          <div className="mb-4 text-sm text-gray-600 dark:text-gray-400 flex items-center gap-3">
            Found {restaurants.length} restaurant{restaurants.length !== 1 ? "s" : ""}
            {searchKeyword && <span>matching "<strong>{searchKeyword}</strong>"</span>}
            <button onClick={() => handleSearch(null)} className="text-orange-500 hover:underline cursor-pointer">Clear</button>
          </div>
        )}

        {loading && <SkeletonGrid />}

        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 text-red-700 dark:text-red-400 border border-red-200 dark:border-red-800 rounded-lg px-4 py-3 text-sm mb-4">{error}</div>
        )}

        {!loading && !error && restaurants.length === 0 && (
          <div className="text-center py-16 text-gray-500 dark:text-gray-400">
            <div className="text-6xl mb-4">🍽️</div>
            <h3 className="text-xl font-semibold mb-2 text-gray-700 dark:text-gray-300">No restaurants found</h3>
            <p>Try a different location or check back later.</p>
          </div>
        )}

        {!loading && !error && restaurants.length > 0 && (
          <>
            {!isSearching && (
              <h2 className="text-xl font-bold text-gray-900 dark:text-white mb-4">
                {location === "ALL" ? "All Restaurants" : `Restaurants in ${location}`}
              </h2>
            )}
            <div className="grid gap-6" style={{ gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))" }}>
              {restaurants.map((r) => (
                isCustomer ? (
                  <RestaurantCard key={r.publicId} restaurant={r}
                    initialFavourited={favRestaurantIds.has(r.publicId)}
                    onFavouriteToggle={handleRestaurantFavouriteToggle}
                    highlight={searchKeyword} />
                ) : (
                  <Link key={r.publicId} to={`/restaurant/${r.publicId}`}
                    className="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md hover:-translate-y-1 hover:shadow-xl transition-all no-underline text-gray-900 dark:text-white block">
                    {r.imageUrl ? (
                      <img src={r.imageUrl} alt={r.name} className="w-full h-44 object-cover" onError={(e) => { e.target.style.display = "none"; }} />
                    ) : (
                      <div className="w-full h-44 bg-gradient-to-br from-orange-100 to-orange-200 flex items-center justify-center text-5xl">🍴</div>
                    )}
                    <div className="p-4">
                      <div className="font-bold text-base mb-1">{r.name}</div>
                      <div className="text-gray-500 dark:text-gray-400 text-sm mb-3 truncate">{r.description}</div>
                    </div>
                  </Link>
                )
              ))}
            </div>
          </>
        )}
      </div>
    </>
  );
};

export default Home;
