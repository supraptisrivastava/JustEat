import { useState } from "react";
import { savePreferences } from "../api/preferencesApi";

const CUISINES = ["INDIAN", "CHINESE", "JAPANESE", "ITALIAN", "MEXICAN", "CONTINENTAL", "FRENCH", "FAST_FOOD"];
const DIETARY = ["VEG", "NON_VEG", "EGG", "VEGAN", "JAIN", "GLUTEN_FREE"];

const PreferencesModal = ({ initial = {}, onClose, onSaved }) => {
  const [cuisines, setCuisines] = useState(initial.favouriteCuisines || []);
  const [dietary, setDietary] = useState(initial.dietaryRestrictions || []);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const toggle = (list, setList, value) => {
    setList((prev) =>
      prev.includes(value) ? prev.filter((v) => v !== value) : [...prev, value]
    );
  };

  const handleSave = async () => {
    setSaving(true);
    setError("");
    try {
      const res = await savePreferences({ favouriteCuisines: cuisines, dietaryRestrictions: dietary });
      if (onSaved) onSaved(res.data);
      onClose();
    } catch {
      setError("Failed to save preferences. Please try again.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm">
      <div className="bg-white dark:bg-gray-900 rounded-2xl shadow-2xl p-6 w-full max-w-md mx-4 max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold text-gray-900 dark:text-white">⚙️ Your Preferences</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-200 text-2xl leading-none">×</button>
        </div>

        <div className="mb-5">
          <h3 className="font-semibold text-gray-700 dark:text-gray-300 mb-3">🍜 Favourite Cuisines</h3>
          <div className="flex flex-wrap gap-2">
            {CUISINES.map((c) => (
              <button
                key={c}
                onClick={() => toggle(cuisines, setCuisines, c)}
                className={`px-3 py-1.5 rounded-full text-sm font-semibold border-2 transition-all cursor-pointer ${
                  cuisines.includes(c)
                    ? "bg-orange-500 border-orange-500 text-white"
                    : "bg-white dark:bg-gray-800 border-gray-200 dark:border-gray-600 text-gray-600 dark:text-gray-400 hover:border-orange-400"
                }`}
              >
                {c.replace("_", " ")}
              </button>
            ))}
          </div>
        </div>

        <div className="mb-6">
          <h3 className="font-semibold text-gray-700 dark:text-gray-300 mb-3">🥗 Dietary Preferences</h3>
          <div className="flex flex-wrap gap-2">
            {DIETARY.map((d) => (
              <button
                key={d}
                onClick={() => toggle(dietary, setDietary, d)}
                className={`px-3 py-1.5 rounded-full text-sm font-semibold border-2 transition-all cursor-pointer ${
                  dietary.includes(d)
                    ? "bg-green-500 border-green-500 text-white"
                    : "bg-white dark:bg-gray-800 border-gray-200 dark:border-gray-600 text-gray-600 dark:text-gray-400 hover:border-green-400"
                }`}
              >
                {d.replace("_", " ")}
              </button>
            ))}
          </div>
        </div>

        {error && (
          <div className="text-red-500 text-sm mb-4">{error}</div>
        )}

        <div className="flex gap-3">
          <button
            onClick={onClose}
            className="flex-1 py-2.5 rounded-xl border-2 border-gray-200 dark:border-gray-600 text-gray-600 dark:text-gray-400 font-semibold hover:border-gray-400 transition-all cursor-pointer"
          >
            Cancel
          </button>
          <button
            onClick={handleSave}
            disabled={saving}
            className="flex-1 py-2.5 rounded-xl bg-orange-500 text-white font-semibold hover:bg-orange-600 transition-all cursor-pointer disabled:opacity-60"
          >
            {saving ? "Saving..." : "Save Preferences"}
          </button>
        </div>
      </div>
    </div>
  );
};

export default PreferencesModal;

