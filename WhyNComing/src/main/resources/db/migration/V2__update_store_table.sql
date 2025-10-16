ALTER TABLE public.stores
    ALTER COLUMN store_rating DROP NOT NULL,
    ALTER COLUMN store_review_count DROP NOT NULL;