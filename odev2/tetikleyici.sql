CREATE OR REPLACE FUNCTION check_captain_availability()
RETURNS TRIGGER AS
$$
DECLARE
    overlapping_count INTEGER;
BEGIN
    -- Yeni atanan kaptanın diğer seferlerdeki görevlerini kontrol et
    SELECT COUNT(*) INTO overlapping_count
    FROM public.voyage_captains AS vc
    JOIN public.voyages AS v ON vc.voyage_id = v.voyageid
    WHERE vc.captain_id = NEW.captain_id
    AND v.departuredate <= (SELECT departuredate FROM public.voyages WHERE voyageid = NEW.voyage_id)
    AND v.returndate >= (SELECT departuredate FROM public.voyages WHERE voyageid = NEW.voyage_id);

    -- Eğer çakışan sefer varsa hata fırlat
    IF overlapping_count > 0 THEN
        RAISE EXCEPTION 'Bu kaptan zaten başka bir seferde görevli.';
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Tetikleyiciyi oluştur
CREATE TRIGGER trigger_check_captain_availability
BEFORE INSERT OR UPDATE ON public.voyage_captains
FOR EACH ROW
EXECUTE FUNCTION check_captain_availability();
 CREATE OR REPLACE FUNCTION check_captain_availability()
RETURNS TRIGGER AS
$$
DECLARE
    overlapping_count INTEGER;
BEGIN
    -- Yeni atanan kaptanın diğer seferlerdeki görevlerini kontrol et
    SELECT COUNT(*) INTO overlapping_count
    FROM public.voyage_captains AS vc
    JOIN public.voyages AS v ON vc.voyage_id = v.voyageid
    WHERE vc.captain_id = NEW.captain_id
    AND v.departuredate <= (SELECT departuredate FROM public.voyages WHERE voyageid = NEW.voyage_id)
    AND v.returndate >= (SELECT departuredate FROM public.voyages WHERE voyageid = NEW.voyage_id);

    -- Eğer çakışan sefer varsa hata fırlat
    IF overlapping_count > 0 THEN
        RAISE EXCEPTION 'Bu kaptan zaten başka bir seferde görevli.';
    END IF;

    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

-- Tetikleyiciyi oluştur
CREATE TRIGGER trigger_check_captain_availability
BEFORE INSERT OR UPDATE ON public.voyage_captains
FOR EACH ROW
EXECUTE FUNCTION check_captain_availability();
