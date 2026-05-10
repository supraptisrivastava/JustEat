import { Link } from "react-router-dom";
import { useState } from "react";
import { toggleRestaurantFavourite } from "../api/favouritesApi";
import { useAuth } from "../context/AuthContext";

const statusCls = (status) => {
  const base = "text-xs font-bold px-2.5 py-0.5 rounded-full uppercase tracking-wide";
  const s = (status || "OPEN").toUpperCase();
  if (s === "OPEN") return `${base} bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-400`;
  if (s === "CLOSED") return `${base} bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400`;
  return `${base} bg-yellow-100 dark:bg-yellow-900/30 text-orange-700 dark:text-orange-400`;
};

const tagCls = "text-xs font-semibold px-2 py-0.5 rounded-full bg-orange-50 dark:bg-orange-900/20 text-orange-500 border border-orange-200 dark:border-orange-700 capitalize";

const RestaurantCard = ({ restaurant, initialFavourited = false, onFavouriteToggle, highlight }) => {
  const { role } = useAuth();
  const [favoured, setFavoured] = useState(initialFavourited);
  const [toggling, setToggling] = useState(false);
  const r = restaurant;

  const handleFavourite = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (toggling) return;
    setToggling(true);
    try {
      await toggleRestaurantFavourite(r.publicId);
      const next = !favoured;
      setFavoured(next);
      if (onFavouriteToggle) onFavouriteToggle(r.publicId, next);
    } catch {
      // silent
    } finally {
      setToggling(false);
    }
  };

  return (
    <Link
      to={`/restaurant/${r.publicId}`}
      className="bg-white dark:bg-gray-800 rounded-xl overflow-hidden shadow-md hover:-translate-y-1 hover:shadow-xl transition-all no-underline text-gray-900 dark:text-white block flex-shrink-0 w-64"
    >
      <div className="relative">
        {r.imageUrl ? (
          <img
            src={r.imageUrl}
            alt={r.name}
            className="w-full h-40 object-cover"
            onError={(e) => { e.target.style.display = "none"; }}
          />
        ) : (
          <div className="w-full h-40 bg-gradient-to-br from-orange-100 to-orange-200 dark:from-orange-900/30 dark:to-orange-800/30 flex items-center justify-center text-5xl">
            🍴
          </div>
        )}
        {role === "CUSTOMER" && (
          <button
            onClick={handleFavourite}
            className={`absolute top-2 right-2 w-8 h-8 rounded-full flex items-center justify-center text-lg shadow-md transition-all ${
              favoured
                ? "bg-red-500 text-white"
                : "bg-white/80 text-gray-400 hover:text-red-500"
            }`}
            title={favoured ? "Remove from favourites" : "Add to favourites"}
          >
            {favoured ? "❤️" : "🤍"}
          </button>
        )}
      </div>
      <div className="p-3">
        <div className="font-bold text-sm mb-1 truncate">
          {highlight ? (
            <span dangerouslySetInnerHTML={{ __html: r.name.replace(new RegExp(`(${highlight})`, "gi"), '<mark class="bg-yellow-200 px-0.5 rounded">$1</mark>') }} />
          ) : r.name}
        </div>
        <div className="text-gray-500 dark:text-gray-400 text-xs mb-2 line-clamp-2">{r.description}</div>
        <div className="flex items-center justify-between gap-1 flex-wrap">
          <div className="flex gap-1 flex-wrap">
            {(r.cuisineTypes || []).slice(0, 2).map((c) => (
              <span key={c} className={tagCls}>{c.replace("_", " ")}</span>
            ))}
          </div>
          <span className={statusCls(r.restaurantStatus)}>{r.restaurantStatus || "OPEN"}</span>
        </div>
        {r.rating != null && (
          <div className="flex items-center gap-1 text-xs font-bold text-orange-700 dark:text-orange-400 mt-1.5">
            ★ {r.rating.toFixed(1)}
            <span className="text-gray-400 font-normal">({r.ratingCount})</span>
          </div>
        )}
      </div>
    </Link>
  );
};

export default RestaurantCard;

