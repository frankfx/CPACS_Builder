package forderungsmanagement.application.insolvenz;

// Imports
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

import kerntabellen.business.BelegArtType;
import kerntabellen.business.BelegPositionsArtType;
import kerntabellen.business.InsoGeldBetragAuswahlType;
import kerntabellen.business.KontoBewegungsTypType;
import kerntabellen.business.PersonenGruppeType;
import kerntabellen.business.RechtskreisType;
import kerntabellen.business.SollHabenKennzeichenType;
import kerntabellen.business.VkStatusType;
import kerntabellen.business.ZvkKontoArtType;
import meldungen.business.VersichertePersonModel;
import meldungen.business.VersichertenMerkmalModel;
import meldungen.business.VersicherungsZeitModel;
import meldungen.business.dienste.MeldungenDiensteFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import partner.business.BetriebsstaetteModel;
import produkt.business.GesetzlicheGrundlagenModel;
import produkt.business.GesetzlicheGrundlagenSearchModel;
import produkt.business.GrundlagenModel;
import produkt.business.GrundlagenSatzungKVModel;
import produkt.business.GrundlagenSatzungKVSearchModel;
import produkt.business.ProduktInterface;
import zahlungsverkehr.business.AusgleichModel;
import zahlungsverkehr.business.AusgleichTeilBetragModel;
import zahlungsverkehr.business.ForderungVerbindlichkeitModel;
import zahlungsverkehr.business.KontoBewegungModel;
import zahlungsverkehr.business.KontoBewegungPositionModel;
import zahlungsverkehr.business.ZahlungsverkehrKontoModel;
import zahlungsverkehr.business.mahnung.SaeumniszuschlagModel;
import zahlungsverkehr.business.util.Intervall;
import zahlungsverkehr.business.util.ZahlungsverkehrConstant;
import baseutil.base.SeverityType;
import baseutil.util.DateUtil;
import beitraege.business.BeitraegeInterface;
import forderungsmanagement.business.ForderungsmanagementMessageCodesEnum;
import forderungsmanagement.business.insolvenz.ArbeitnehmerZeitModel;
import forderungsmanagement.business.insolvenz.ForderungInsolvenzModel;
import forderungsmanagement.business.insolvenz.ForderungInsolvenzViewModel;
import forderungsmanagement.business.insolvenz.InsolvenzModel;
import forderungsmanagement.business.insolvenz.InsolvenzgeldAntragModel;
import forderungsmanagement.business.insolvenz.InsolvenzgeldModel;
import forderungsmanagement.business.insolvenz.LohnabrechnungModel;
import forderungsmanagement.business.insolvenz.RueckstandViewModel;
import forderungsmanagement.service.insolvenz.InsolvenzService;
import forderungsmanagement.service.insolvenz.InsolvenzService.InsolvenzBetraege;
import forderungsmanagement.service.insolvenz.LohnabrechnungService;
import forderungsmanagement.service.insolvenz.LohnabrechnungService.Beitragsbemessungsgrenzen;
import forderungsmanagement.service.insolvenz.LohnabrechnungService.GesetzlicheGrundlagen;
import forderungsmanagement.service.insolvenz.LohnabrechnungService.Lohndaten;
import framework.base.DecimalUtil;
import framework.base.assertion.Assert;
import framework.business.Context;
import framework.business.FinderResult;
import framework.business.TemporalModel;
import framework.business.search.ScrollableResults;
import framework.service.ServiceFactory;

public class InsolvenzPaUtil extends Object {

	// @formatter:off
	private static final Collection<ZvkKontoArtType> BERUECKSICHTIGTE_LISTEKONTEN = Arrays.asList(ZvkKontoArtType.LISTEKONTOC, ZvkKontoArtType.LISTEKONTOB);
	
	private static final Collection<VkStatusType> VK_STATUS_STORNIERT_GELOESCHT = Arrays.asList(
			VkStatusType.STORNIERT, 
			VkStatusType.GELOESCHT
			);
	
	private static final Collection<BelegPositionsArtType> BELEGPOSITIONSARTEN_PV_BEITRAG = Arrays.asList(
			BelegPositionsArtType.PV0001
			);
	
	private static final Collection<BelegPositionsArtType> BELEGPOSITIONSARTEN_PFLZB_ZUSATZBEITRAG= Arrays.asList(
			BelegPositionsArtType.PFLZB
			);

	private static final Collection<BelegPositionsArtType> BELEGPOSITIONSARTEN_RV_BEITRAG = Arrays.asList(
			BelegPositionsArtType.RV0100, 
			BelegPositionsArtType.RV0200, 
			BelegPositionsArtType.RV0300,
			BelegPositionsArtType.RV0400, 
			BelegPositionsArtType.RV0500, 
			BelegPositionsArtType.RV0600, 
			BelegPositionsArtType.RVGERINGF, 
			BelegPositionsArtType.RVHALB,
			BelegPositionsArtType.RVVOLL
			);

	private static final Collection<BelegPositionsArtType> BELEGPOSITIONSARTEN_AV_BEITRAG = Arrays.asList(
			BelegPositionsArtType.AF0010, 
			BelegPositionsArtType.AF0020
			);

	private static final Collection<BelegPositionsArtType> BELEGPOSITIONSARTEN_KV_BEITRAG = Arrays.asList(
			BelegPositionsArtType.KV1000, 
			BelegPositionsArtType.KV1000KORREKTUR,
			BelegPositionsArtType.KV1000VOR2009, 
			BelegPositionsArtType.KV2000,
			BelegPositionsArtType.KV2000KORREKTUR, 
			BelegPositionsArtType.KV2000VOR2009, 
			BelegPositionsArtType.KV3000, 
			BelegPositionsArtType.KV3000KORREKTUR,
			BelegPositionsArtType.KV3000VOR2009, 
			BelegPositionsArtType.KV6000, 
			BelegPositionsArtType.KV6000KORREKTUR, 
			BelegPositionsArtType.KV6000VOR2009
			);
	
	private static final Collection<BelegArtType> RELEVANTE_BELEGARTEN_MIT_TEILMONATSBERECHNUNG = Arrays.asList(
			BelegArtType.GESCHAETZTERBEITRAGSNACHWEIS, 
			BelegArtType.NORMALERBEITRAGSNACHWEISGDBN,
			BelegArtType.NORMALERBEITRAGSNACHWEISDBN,
			BelegArtType.NORMALERBEITRAGSNACHWEISUDBNEG
			);
	
	private static final Collection<BelegArtType> RELEVANTE_BELEGARTEN_OHNE_TEILMONATSBERECHNUNG = Arrays.asList(
			BelegArtType.BEITRAGSNACHWEISAGSAMMELKONTO,
			BelegArtType.BEITRAGSNACHWEISBETRIEBSPRUEF,
			BelegArtType.NORMALERBEITRAGSNACHWEISUDBN,
			BelegArtType.BEITRAGSNACHWEISWERTGUTH,
			BelegArtType.KORREKTURBEITRAGSNACHWEIS,
			BelegArtType.KORREKTURBEITRNWVOR2009,
			BelegArtType.MIGRVSLBBEITRAGGSV,
			BelegArtType.MIGRVSLCBEITRAGGSV,
			BelegArtType.MIGRVSLDBEITRAGGSV,
			BelegArtType.MIGRVSLEBEITRAGGSV,
			BelegArtType.NORMALERBEITRAGSNACHWEIS,
			BelegArtType.VSBEITRAGGSV,
			BelegArtType.KORREKTURBEITRAGSNACHWEIS,
			BelegArtType.NORMALERBEITRAGSNACHWEISNEG,
			BelegArtType.VSBEITRAGNEGGSV,
			BelegArtType.BEITRAEGEMITGLIEDNEG,
			BelegArtType.KORREKTURBEITRNWNEGVOR2009
			);
	// @formatter:on

	private static final int EINS = 1;
	private static final BigDecimal ZWEI = new BigDecimal("2.0");
	private static final BigDecimal DREISSIG = new BigDecimal("30.0");
	private static final BigDecimal HUNDERT = new BigDecimal("100.0");

	private static class MonatsBeitragModel {
		private Date mBezugVon;
		private Date mBezugBis;
		private BigDecimal mBetrag;
		private int mAnzahlTageImBankjahr;

		public Date getBezugVon() {
			return this.mBezugVon;
		}

		public void setBezugVon(Date pBezugVon) {
			this.mBezugVon = pBezugVon;
		}

		public Date getBezugBis() {
			return this.mBezugBis;
		}

		public void setBezugBis(Date pBezugBis) {
			this.mBezugBis = pBezugBis;
		}

		public BigDecimal getBetrag() {
			return this.mBetrag;
		}

		public void setBetrag(BigDecimal pBetrag) {
			this.mBetrag = pBetrag;
		}

		public int getAnzahlTageImBankjahr() {
			return this.mAnzahlTageImBankjahr;
		}

		public void setAnzahlTageImBankjahr(int pAnzahlTageImBankjahr) {
			this.mAnzahlTageImBankjahr = pAnzahlTageImBankjahr;
		}
	}

	public static boolean validiereBeginnUndEndeDatum(Context pContext, VersicherteViewModel pVersicherteViewModel) {
		pVersicherteViewModel.clearErrors();
		if (!pContext.getMessageList().getHasNewEvents()) {
			pContext.getMessageList().clearEvents();
		}
		return pVersicherteViewModel.validateManuelleEingaben(true);
	}

	public static VersicherteViewModel bearbeiteArbeitnehmer(Context pContext, InsolvenzModel pInsolvenz, Collection<VersicherteViewModel> pVersicherteView, int pZuordnung,
			InsolvenzgeldAntragModel pInsolvenzgeldAntrag) {
		VersicherteViewModel ersterFehlerhafterSatz = null;
		int anzAN = 0;
		int anzANMitKind = 0;

		if (pInsolvenz != null && pInsolvenz.getZvKonto() != null) {
			BigDecimal anBetrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

			if (CollectionUtils.isNotEmpty(pVersicherteView)) {
				for (VersicherteViewModel versicherteView : pVersicherteView) {
					ArbeitnehmerZeitModel versicherungsZeit = versicherteView.getGemeldeteZeit();
					if (!(validiereBeginnUndEndeDatum(pContext, versicherteView)) && ersterFehlerhafterSatz == null) {
						ersterFehlerhafterSatz = versicherteView;
					}

					if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD) {
						if (versicherteView.getAusgewaehlt()) {
							if (!versicherteView.isArbeitnehmerAnteilNull()) {
								anBetrag = DecimalUtil.add(anBetrag, versicherteView.getArbeitnehmerAnteil());
							}

							if (!versicherteView.isHatKinderNull() && versicherteView.getHatKinder()) {
								anzANMitKind++;
							}
							anzAN++;
						}
					}

					// ggf. neue Zuordnung herstellen
					versicherungsZeit = setzeArbeitnehmerZeitModel(versicherteView, pInsolvenz, pZuordnung).getGemeldeteZeit();
					if (versicherungsZeit != null && !versicherteView.isAusgewaehltNull() && !versicherteView.getAusgewaehlt() && !versicherungsZeit.isModelOidNull()) {
						// Vorhandene Zuordnung löschen
						versicherteView.setGemeldeteZeit(null);
						versicherungsZeit.setArbeitnehmer(null);
						versicherungsZeit.setInsolvenz(null);
						versicherungsZeit.setBetriebspruefung(null);
						versicherungsZeit.setInsolvenzgeld(null);
						versicherungsZeit.remove();

					} else if (versicherungsZeit != null && !versicherteView.isAusgewaehltNull() && versicherteView.getAusgewaehlt()) {
						// Änderungen übernehmen
						if (versicherungsZeit.getBeginnDatum() != null) {
							if (versicherteView.getBeginnDatum() != null) {
								if (versicherungsZeit.getBeginnDatum().compareTo(versicherteView.getBeginnDatum()) != 0) {
									versicherungsZeit.setBeginnDatum(versicherteView.getBeginnDatum());
								}
							} else {
								versicherungsZeit.setBeginnDatumNull();
							}
						} else {
							versicherungsZeit.setBeginnDatum(versicherteView.getBeginnDatum());
						}
						if (versicherungsZeit.getEndeDatum() != null) {
							if (versicherteView.getEndeDatum() != null) {
								if (versicherungsZeit.getEndeDatum().compareTo(versicherteView.getEndeDatum()) != 0) {
									versicherungsZeit.setEndeDatum(versicherteView.getEndeDatum());
								}
							} else {
								versicherungsZeit.setEndeDatumNull();
							}
						} else {
							versicherungsZeit.setEndeDatum(versicherteView.getEndeDatum());
						}
						if (versicherungsZeit.getArbeitnehmerAnteil() != null) {
							if (versicherteView.getArbeitnehmerAnteil() != null) {
								if (versicherungsZeit.getArbeitnehmerAnteil().compareTo(versicherteView.getArbeitnehmerAnteil()) != 0) {
									versicherungsZeit.setArbeitnehmerAnteil(versicherteView.getArbeitnehmerAnteil());
								}
							} else {
								versicherungsZeit.setArbeitnehmerAnteilNull();
							}
						} else {
							versicherungsZeit.setArbeitnehmerAnteil(versicherteView.getArbeitnehmerAnteil());
						}

						if (!versicherteView.isUnkenntnisInsolvenzNull()) {
							versicherungsZeit.setUnkenntnisInsolvenz(versicherteView.getUnkenntnisInsolvenz());
						}
					}
				}
			}

			if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD) {
				if (pInsolvenzgeldAntrag != null) {
					if (anzAN == 0) {
						pInsolvenzgeldAntrag.setAnzahlArbeitnehmerNull();
					} else {
						if (pInsolvenzgeldAntrag.isAnzahlArbeitnehmerNull() || pInsolvenzgeldAntrag.getAnzahlArbeitnehmer() != anzAN) {
							pInsolvenzgeldAntrag.setAnzahlArbeitnehmer(anzAN);
						}
					}

					if (anzANMitKind == 0) {
						pInsolvenzgeldAntrag.setAnzahlArbeitnehmerMitKindNull();
					} else {
						if (pInsolvenzgeldAntrag.isAnzahlArbeitnehmerMitKindNull() || pInsolvenzgeldAntrag.getAnzahlArbeitnehmerMitKind() != anzANMitKind) {
							pInsolvenzgeldAntrag.setAnzahlArbeitnehmerMitKind(anzANMitKind);
						}
					}
				}
			}
		}
		return ersterFehlerhafterSatz;
	}

	public static VersicherteViewModel setzeArbeitnehmerZeitModel(VersicherteViewModel pVersicherter, InsolvenzModel pInsolvenz, int pZuordnung) {
		VersicherteViewModel lResult = pVersicherter;

		if (lResult.getGemeldeteZeit() == null && !lResult.isAusgewaehltNull() && lResult.isAusgewaehlt()) {
			ArbeitnehmerZeitModel lArbeitnehmerzeit = new ArbeitnehmerZeitModel(pInsolvenz.getContext(), true);
			lArbeitnehmerzeit.setArbeitnehmer(lResult.getVersicherungsZeit().getVersichertePerson().getNatuerlichePerson());
			lArbeitnehmerzeit.setModelOid(pVersicherter.getVersicherungsZeit().getOid());

			if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZ) {
				lArbeitnehmerzeit.setInsolvenz(pInsolvenz);
			} else if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD) {
				lArbeitnehmerzeit.setInsolvenzgeld(pInsolvenz.getInsolvenzgeld());
			} else if (pZuordnung == InsolvenzService.ZUORDNUNG_BETRIEBSPRUEFUNG) {
				lArbeitnehmerzeit.setBetriebspruefung(pInsolvenz.getBetriebspruefung());
			}

			lResult.setGemeldeteZeit(lArbeitnehmerzeit);
		}
		return lResult;
	}

	public static Collection<BeitragsschaetzungenViewModel> ermittleBeitragsschaetzungen(Context pContext, InsolvenzModel pInsolvenz) {
		Collection<BeitragsschaetzungenViewModel> result = Collections.emptyList();

		if (pInsolvenz != null && pInsolvenz.getZvKonto() != null) {
			result = new ArrayList<>();

			Date referenzdatum = (pInsolvenz.getBetriebspruefung() == null || pInsolvenz.getBetriebspruefung().isAntragAmNull()) ? ServiceFactory.getInstance(InsolvenzService.class)
					.getDatumInsolvenzEreignis(pInsolvenz) : pInsolvenz.getBetriebspruefung().getAntragAm();

			Collection<KontoBewegungModel> lKontobewegungen = ServiceFactory.getInstance(InsolvenzService.class).ermittleBeitragsschaetzungen(pContext, pInsolvenz.getZvKonto(), referenzdatum);
			for (KontoBewegungModel lKB : lKontobewegungen) {
				BeitragsschaetzungenViewModel beitragsschaetzung = new BeitragsschaetzungenViewModel(pContext);
				beitragsschaetzung.setKontobewegung(lKB);
				result.add(beitragsschaetzung);
			}
		}
		return result;
	}

	public static Intervall ermittleRueckstandszeitraum(Context pContext, InsolvenzgeldAntragModel pInsolvenzgeldAntrag) {
		Date bezugVonAeltesterRueckstand = null;
		Date bezugBisJuengsterRueckstand = null;
		Collection<RueckstandViewModel> rueckstaende = ermittleRueckstaende(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz(), null, null, pInsolvenzgeldAntrag);
		for (RueckstandViewModel lRueckstand : rueckstaende) {
			KontoBewegungModel kb = lRueckstand.getKontobewegung();
			if (kb != null) {
				if (bezugVonAeltesterRueckstand == null) {
					bezugVonAeltesterRueckstand = kb.getBezugVon();
				} else if (bezugVonAeltesterRueckstand.after(kb.getBezugVon())) {
					bezugVonAeltesterRueckstand = kb.getBezugVon();
				}

				if (bezugBisJuengsterRueckstand == null) {
					bezugBisJuengsterRueckstand = kb.getBezugBis();
				} else if (bezugBisJuengsterRueckstand.before(kb.getBezugVon())) {
					bezugBisJuengsterRueckstand = kb.getBezugBis();
				}
			}
		}
		return new Intervall(bezugVonAeltesterRueckstand, bezugBisJuengsterRueckstand);
	}

	public static Collection<ForderungInsolvenzViewModel> ermittleForderungenUndSetzeAnAnteil(Context pContext, InsolvenzModel pInsolvenz, boolean pNurZugeordneteAnzeigen, int pZuordnung) {
		Collection<ForderungInsolvenzViewModel> result = ServiceFactory.getInstance(InsolvenzService.class).ermittleForderungen(pContext, pInsolvenz, pNurZugeordneteAnzeigen, pZuordnung);

		if (result != null) {
			InsolvenzService lService = ServiceFactory.getInstance(InsolvenzService.class);
			Date lReferenzdatum = lService.getDatumForderungsermittlung(pInsolvenz);
			Map<Class<? extends GrundlagenModel>, GrundlagenModel> lGrundlagen = getGrundlagen(pContext, lReferenzdatum, lReferenzdatum);
			GesetzlicheGrundlagenModel lGesetzlicheGrundlage = retrieve(GesetzlicheGrundlagenModel.class, lGrundlagen);
			GrundlagenSatzungKVModel lGrundlageKV = retrieve(GrundlagenSatzungKVModel.class, lGrundlagen);

			for (ForderungInsolvenzViewModel lRelevanteForderung : result) {
				lRelevanteForderung.setBetragAnAnteil(ermittleANAnteilZurForderung(lRelevanteForderung.getFordVerb(), lGesetzlicheGrundlage, lGrundlageKV));
			}
		}
		return result;
	}

	public static Collection<RueckstandViewModel> ermittleRueckstaende(Context pContext, InsolvenzModel pInsolvenz, Date pRueckstaendeAb, Date pRueckstaendeBis,
			InsolvenzgeldAntragModel pInsolvenzgeldAntrag) {
		Collection<RueckstandViewModel> lRueckstaende = new ArrayList<>();
		RueckstandViewModel lRueckstandSumme = null;

		if (pInsolvenz != null && pInsolvenz.getZvKonto() != null) {
			lRueckstandSumme = getNewRueckstandModel(pContext);
			// PCR 128487: Änderung der Ermittlung des Referenzdatums
			// Date referenzDatum = InsolvenzUtil.getDatumForderungsermittlung(pInsolvenz);

			Date rueckstaendeAb = pRueckstaendeAb == null ? DateUtil.newMinValue() : pRueckstaendeAb;
			Date rueckstaendeBis = pRueckstaendeBis == null ? DateUtil.newMaxValue() : pRueckstaendeBis;

			Collection<BelegArtType> relevanteBelegarten = new ArrayList<>();
			relevanteBelegarten.addAll(RELEVANTE_BELEGARTEN_OHNE_TEILMONATSBERECHNUNG);
			relevanteBelegarten.addAll(RELEVANTE_BELEGARTEN_MIT_TEILMONATSBERECHNUNG);

			Integer anzAn = null;
			Integer anzAnKinderlos = null;
			Integer anzAnMitKind = null;
			if (pInsolvenzgeldAntrag != null) {
				anzAn = pInsolvenzgeldAntrag.getAnzahlArbeitnehmerAsObject();
				anzAnMitKind = pInsolvenzgeldAntrag.getAnzahlArbeitnehmerMitKindAsObject();

				if (anzAn != null && anzAn <= 0) {
					anzAn = null;
				}
				if (anzAnMitKind != null && anzAnMitKind <= 0) {
					anzAnMitKind = null;
				}
				if (anzAn != null && anzAnMitKind != null) {
					anzAnKinderlos = anzAn - anzAnMitKind;

					if (anzAnKinderlos < 0) {
						anzAnKinderlos = null;
					}
				}
			}

			// Kontobewegungen ermitteln und in die gewünschte Form konvertieren
			Collection<KontoBewegungModel> kbOffen = ServiceFactory.getInstance(InsolvenzService.class).sucheOffeneKontobewegungenInDB(pContext, pInsolvenz.getZvKonto(), null, null,
					relevanteBelegarten, null);
			Map<KontoBewegungModel, RueckstandViewModel> kbRueckstaende = new HashMap<>();
			for (KontoBewegungModel kb : kbOffen) {
				// Nur die Kontobewegungen, die mit ihrem Bezugszeitraum (nicht mit der Fälligkeit!) in den geforderten Rückstandszeitraum gehören, berücksichtigen.
				if (!kb.getBezugBis().before(rueckstaendeAb) && !kb.getBezugVon().after(rueckstaendeBis)) {
					boolean reduzieren = RELEVANTE_BELEGARTEN_MIT_TEILMONATSBERECHNUNG.contains(kb.getBelegArt());
					Map<Class<? extends GrundlagenModel>, GrundlagenModel> lGrundlagen = getGrundlagen(pContext, kb.getBezugVon(), kb.getBezugBis());
					RueckstandViewModel lRueckstand = getRueckstand(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld(), kb, anzAn, anzAnKinderlos, lRueckstandSumme, lGrundlagen, null, pRueckstaendeAb,
							pRueckstaendeBis, reduzieren);
					if (lRueckstand != null) {
						lRueckstand.setBelegArtText(kb.getBelegArt().getShortDescription());
						lRueckstaende.add(lRueckstand);
						kbRueckstaende.put(kb, lRueckstand);
					}
				}
			}

			Map<KontoBewegungModel, String> kbListekonto = ServiceFactory.getInstance(InsolvenzService.class).ermittleKBListeKonto(pContext, pInsolvenz.getZvKonto(), BERUECKSICHTIGTE_LISTEKONTEN,
					rueckstaendeAb, relevanteBelegarten);
			for (Map.Entry<KontoBewegungModel, String> keyValueKb : kbListekonto.entrySet()) {
				KontoBewegungModel kb = keyValueKb.getKey();
				String kbBelegText = keyValueKb.getValue();
				Map<KontoBewegungModel, BigDecimal> urspruenge = ServiceFactory.getInstance(InsolvenzService.class).ermittleUrsprungVonAbsetzung(kb);
				// Die Ursprünglichen Kontobewegungen der Absetzungen anzeigen.
				// Sind keine Ursprünge vorhanden, so ist die Buchung der Absetzung anzuzeigen
				if (MapUtils.isNotEmpty(urspruenge)) {
					for (Map.Entry<KontoBewegungModel, BigDecimal> keyValue : urspruenge.entrySet()) {
						KontoBewegungModel kbOrig = keyValue.getKey();
						BigDecimal absetzungBetrag = keyValue.getValue();
						// Bei Teilabsetzung nur den Text der Original-KB erweitern
						if (kbRueckstaende.containsKey(kbOrig)) {
							RueckstandViewModel lRueckstand = kbRueckstaende.get(kbOrig);
							lRueckstand.setBelegArtText(kbOrig.getBelegArt().getShortDescription() + " (Liste" + StringUtils.substringAfter(kbBelegText, "(Liste"));
						} else {
							// Nur die Kontobewegungen, die mit ihrem Bezugszeitraum (nicht mit der Fälligkeit!) in den geforderten Rückstandszeitraum gehören, berücksichtigen.
							if (!kbOrig.getBezugBis().before(rueckstaendeAb) && !kbOrig.getBezugVon().after(rueckstaendeBis)) {
								boolean reduzieren = RELEVANTE_BELEGARTEN_MIT_TEILMONATSBERECHNUNG.contains(kbOrig.getBelegArt());
								Map<Class<? extends GrundlagenModel>, GrundlagenModel> lGrundlagen = getGrundlagen(pContext, kbOrig.getBezugVon(), kbOrig.getBezugBis());
								RueckstandViewModel lRueckstand = getRueckstand(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld(), kbOrig, anzAn, anzAnKinderlos, lRueckstandSumme, lGrundlagen,
										absetzungBetrag, rueckstaendeAb, rueckstaendeBis, reduzieren);
								if (lRueckstand != null) {
									lRueckstand.setBelegArtText(kbOrig.getBelegArt().getShortDescription() + " (Liste" + StringUtils.substringAfter(kbBelegText, "(Liste"));
									lRueckstaende.add(lRueckstand);
								}
							}
						}
					}
				} else {
					// Nur die Absetzungen, die mit ihrem Bezugszeitraum (nicht mit der Fälligkeit!) in den geforderten Rückstandszeitraum gehören, berücksichtigen.
					if (kb.getKontoBewegungPosition().size() > 0 && !kb.getBezugBis().before(rueckstaendeAb) && !kb.getBezugVon().after(rueckstaendeBis)) {
						boolean reduzieren = RELEVANTE_BELEGARTEN_MIT_TEILMONATSBERECHNUNG.contains(kb.getBelegArt());
						Map<Class<? extends GrundlagenModel>, GrundlagenModel> lGrundlagen = getGrundlagen(pContext, kb.getBezugVon(), kb.getBezugBis());
						RueckstandViewModel lRueckstand = getRueckstand(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld(), kb, anzAn, anzAnKinderlos, lRueckstandSumme, lGrundlagen,
								kb.getOffenerBetrag(), rueckstaendeAb, rueckstaendeBis, reduzieren);
						if (lRueckstand != null) {
							lRueckstand.setBelegArtText(kbBelegText);
							lRueckstaende.add(lRueckstand);
						}
					}
				}
			}
		} else {
			if (pInsolvenz != null) {
				lRueckstandSumme = getNewRueckstandModel(pContext);
			}
		}
		lRueckstaende.add(lRueckstandSumme);

		return lRueckstaende;
	}

	private static void setRueckstandManuell(RueckstandViewModel pRueckstand, InsolvenzgeldModel pInsolvenzgeld) {
		for (ForderungInsolvenzModel lForderungInsolvenz : pInsolvenzgeld.getForderungen()) {
			if ((pRueckstand.getKontobewegung() != null && lForderungInsolvenz.getFordVerb() != null && lForderungInsolvenz.getFordVerb().equals(
					pRueckstand.getKontobewegung().getForderungVerbindlichkeit()))) {
				pRueckstand.setBetragManuell(lForderungInsolvenz.getBetrag());
				pRueckstand.setForderungBetragManuell(lForderungInsolvenz);
				break;
			}
		}
		if (pRueckstand.isBetragManuellNull()) {
			pRueckstand.setBetragManuell(pRueckstand.getSumme());
		}
	}

	public static void checkRueckstandManuell(Collection<RueckstandViewModel> pRueckstaende, InsolvenzgeldModel pInsolvenzgeld) {
		Collection<ForderungInsolvenzModel> lGueltigeManuelleBetraege = new ArrayList<>();
		Iterator<RueckstandViewModel> lRSIt = pRueckstaende.iterator();
		while (lRSIt.hasNext()) {
			RueckstandViewModel lRueckstand = lRSIt.next();
			if (lRueckstand.getBetragManuell().equals(lRueckstand.getSumme())) {
				if (lRueckstand.getForderungBetragManuell() != null) {
					ForderungInsolvenzModel lForderungInsolvenz = lRueckstand.getForderungBetragManuell();
					lRueckstand.setForderungBetragManuell(null);
					if (pInsolvenzgeld.getForderungen().contains(lForderungInsolvenz)) {
						pInsolvenzgeld.removeForderungen(lForderungInsolvenz);
					}
					lForderungInsolvenz.remove();
				}
			} else {
				if (lRueckstand.getForderungBetragManuell() == null) {
					ForderungInsolvenzModel lForderungInsolvenz = new ForderungInsolvenzModel(pInsolvenzgeld.getContext(), pInsolvenzgeld.isPersistent());
					lForderungInsolvenz.setBetrag(lRueckstand.getBetragManuell());
					lForderungInsolvenz.setFordVerb(lRueckstand.getKontobewegung().getForderungVerbindlichkeit());
					pInsolvenzgeld.addForderungen(lForderungInsolvenz);
					lGueltigeManuelleBetraege.add(lForderungInsolvenz);
				} else {
					lRueckstand.getForderungBetragManuell().setBetrag(lRueckstand.getBetragManuell());
					lGueltigeManuelleBetraege.add(lRueckstand.getForderungBetragManuell());
				}
			}
		}
		Iterator<ForderungInsolvenzModel> lFIIt = pInsolvenzgeld.getForderungen().iterator();
		while (lFIIt.hasNext()) {
			ForderungInsolvenzModel lForderungInsolvenz = lFIIt.next();
			if (!lGueltigeManuelleBetraege.contains(lForderungInsolvenz)) {
				pInsolvenzgeld.removeForderungen(lForderungInsolvenz);
				lForderungInsolvenz.remove();
			}
		}
	}

	private static <T> T retrieve(Class<? extends T> pClass, Map<Class<? extends GrundlagenModel>, GrundlagenModel> pGrundlagen) {
		return pClass.cast(pGrundlagen.get(pClass));

	}

	private static Date ermittleBisDatumAusAktuellemRueckstandEinzelAufstellung(InsolvenzgeldAntragModel pAntrag, Intervall pIntervall3Monate) {
		Date bis = null;

		Collection<RueckstandViewModel> lRueckstandAufstellung = InsolvenzPaUtil.ermittleRueckstaende(pAntrag.getContext(), pAntrag.getInsolvenzgeld().getInsolvenz(), pAntrag.getRueckstaendigVon(),
				pAntrag.getRueckstaendigBis(), pAntrag);

		for (RueckstandViewModel rueckstand : lRueckstandAufstellung) {
			if (rueckstand.getKontobewegung() != null && !rueckstand.getBetragManuell().equals(ZahlungsverkehrConstant.ZEROBIGDECIMAL)) {
				KontoBewegungModel kb = rueckstand.getKontobewegung();
				if (kb != null) {
					if (bis == null) {
						bis = kb.getBezugBis();
					} else if (bis.before(kb.getBezugBis())) {
						bis = kb.getBezugBis();
					}
				}
			}
		}

		if (bis != null && pIntervall3Monate != null) {
			if (pIntervall3Monate.getBis() != null && bis.after(pIntervall3Monate.getBis())) {
				bis = pIntervall3Monate.getBis();
			}
		}

		return bis;
	}

	private static Intervall ermittleZeitraumAusAktuellemRueckstandEinzelAufstellung(InsolvenzgeldAntragModel pAntrag, Intervall pIntervall3Monate) {
		Date bis = ermittleBisDatumAusAktuellemRueckstandEinzelAufstellung(pAntrag, pIntervall3Monate);
		Date von = null;

		if (bis != null) {
			Collection<RueckstandViewModel> lRueckstandAufstellung = InsolvenzPaUtil.ermittleRueckstaende(pAntrag.getContext(), pAntrag.getInsolvenzgeld().getInsolvenz(),
					pAntrag.getRueckstaendigVon(),
					pAntrag.getRueckstaendigBis(), pAntrag);

			Date lMinDatumIm3MonatsIntevall = DateUtil.add(DateUtil.subtractMonth(bis, 3), 1);

			for (RueckstandViewModel rueckstand : lRueckstandAufstellung) {
				if (rueckstand.getKontobewegung() != null && !rueckstand.getBetragManuell().equals(ZahlungsverkehrConstant.ZEROBIGDECIMAL)) {
					KontoBewegungModel kb = rueckstand.getKontobewegung();
					if (kb != null) {
						Date datum = kb.getBezugVon();

						if (datum.before(lMinDatumIm3MonatsIntevall)) {
							return new Intervall(lMinDatumIm3MonatsIntevall, bis);
						}

						boolean datumLiegtIm3MonatsIntervall = !datum.before(lMinDatumIm3MonatsIntevall) && !datum.after(bis);

						if (von == null) {
							if (datumLiegtIm3MonatsIntervall) {
								von = datum;
							}
						} else if (datum.before(von) && datumLiegtIm3MonatsIntervall) {
							von = datum;
						}
					}
				}
			}
			if (von == null) {
				von = lMinDatumIm3MonatsIntevall;
			}
		}
		return new Intervall(von, bis);
	}

	private static Intervall ermittleZeitraumAusAktuellemRueckstand(InsolvenzgeldAntragModel pAntrag, Intervall pIntervall3Monate) {
		Date bis = null;
		Date von = null;

		Collection<RueckstandViewModel> lRueckstandAufstellung = InsolvenzPaUtil.ermittleRueckstaende(pAntrag.getContext(), pAntrag.getInsolvenzgeld().getInsolvenz(), pAntrag.getRueckstaendigVon(),
				pAntrag.getRueckstaendigBis(), pAntrag);
		for (RueckstandViewModel rueckstand : lRueckstandAufstellung) {
			if (rueckstand.getKontobewegung() != null && !rueckstand.getBetragManuell().equals(ZahlungsverkehrConstant.ZEROBIGDECIMAL)) {
				KontoBewegungModel kb = rueckstand.getKontobewegung();
				if (kb != null) {
					if (von == null) {
						von = kb.getBezugVon();
					} else if (von.after(kb.getBezugVon())) {
						von = kb.getBezugVon();
					}

					if (bis == null) {
						bis = kb.getBezugBis();
					} else if (bis.before(kb.getBezugBis())) {
						bis = kb.getBezugBis();
					}
				}
			}
		}

		if (pIntervall3Monate != null) {
			if (von == null || (pIntervall3Monate.getVon() != null && von.before(pIntervall3Monate.getVon()))) {
				von = pIntervall3Monate.getVon();
			}
			if (bis == null || (pIntervall3Monate.getBis() != null && bis.after(pIntervall3Monate.getBis()))) {
				bis = pIntervall3Monate.getBis();
			}
		}

		return new Intervall(von, bis);
	}

	public static BigDecimal ermittleBetragAusAktuellemRueckstand(InsolvenzgeldAntragModel pAntrag) {
		BigDecimal result = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

		Collection<RueckstandViewModel> lRueckstandAufstellung = InsolvenzPaUtil.ermittleRueckstaende(pAntrag.getContext(), pAntrag.getInsolvenzgeld().getInsolvenz(), pAntrag.getRueckstaendigVon(),
				pAntrag.getRueckstaendigBis(), pAntrag);
		for (RueckstandViewModel rueckstand : lRueckstandAufstellung) {
			if (!rueckstand.isBetragManuellNull()) {
				result = DecimalUtil.add(result, rueckstand.getBetragManuell());
			}
		}

		return result;
	}

	public static Intervall ermittleRueckstandszeitraumNachBetragsart(Context pContext, InsolvenzgeldAntragModel pAntrag) {
		Date von = null;
		Date bis = null;

		if (pAntrag != null && !pAntrag.isRueckstaendigeBeitraegeBetragAuswahlNull()) {
			InsolvenzService lInsolvenzService = ServiceFactory.getInstance(InsolvenzService.class);
			Intervall lRueckstandszeitraum = ermittleRueckstandszeitraum(pContext, pAntrag);
			Intervall lIntervall3Monate = lInsolvenzService.ermittleArbeitnehmerRueckstandszeitraum(lInsolvenzService.getDatumForderungsermittlung(pAntrag.getInsolvenzgeld().getInsolvenz()),
					DateUtil.newMinValue(), DateUtil.newMaxValue(), InsolvenzService.ZUORDNUNG_INSOLVENZGELD, lRueckstandszeitraum.getVon());

			InsoGeldBetragAuswahlType betragsart = pAntrag.getRueckstaendigeBeitraegeBetragAuswahl();
			if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGARBEITNEHMER)) {
				Intervall lAnIntervall = lInsolvenzService.ermittleZeitraumAusZugeordnetenInsolvenzgeldArbeitnehmern(pAntrag.getInsolvenzgeld(), lIntervall3Monate);
				von = lAnIntervall.getVon();
				bis = lAnIntervall.getBis();
			} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGEN)) {
				Intervall lFordIntervall = ermittleZeitraumAusAktuellemRueckstandEinzelAufstellung(pAntrag, lIntervall3Monate);
				von = lFordIntervall.getVon();
				bis = lFordIntervall.getBis();
			} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGARBEITNEHMERFORDERUNGEN)) {
				Intervall lAnIntervall = lInsolvenzService.ermittleZeitraumAusZugeordnetenInsolvenzgeldArbeitnehmern(pAntrag.getInsolvenzgeld(), lIntervall3Monate);
				Intervall lFordIntervall = ermittleZeitraumAusAktuellemRueckstand(pAntrag, lIntervall3Monate);
				boolean lFordVorhanden = !pAntrag.isRueckstaendigeBeitraegeForderungenNull();
				boolean lAnVorhanden = !pAntrag.isRueckstaendigeBeitraegeArbeitnehmerNull();
				if (lAnIntervall.getVon() != null && lAnIntervall.getBis() != null && lFordIntervall.getVon() != null && lFordIntervall.getBis() != null && lFordVorhanden && lAnVorhanden) {
					if (lFordIntervall.getVon().before(lAnIntervall.getVon())) {
						von = lFordIntervall.getVon();
					} else {
						von = lAnIntervall.getVon();
					}
					if (lFordIntervall.getBis().after(lAnIntervall.getBis())) {
						bis = lFordIntervall.getBis();
					} else {
						bis = lAnIntervall.getBis();
					}
				} else if (!lFordVorhanden && !lAnVorhanden) {
					// Erstmalige Belegung mit dem Dreimonatszeitraum
					von = lIntervall3Monate.getVon();
					bis = lIntervall3Monate.getBis();
				} else {
					// Solange die Vorbelegung beibehalten, bis beides ausgewählt wurde
					von = pAntrag.getRueckstaendigVon();
					bis = pAntrag.getRueckstaendigBis();
				}
			} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGEN3MONATE)) {
				von = lIntervall3Monate.getVon();
				bis = lIntervall3Monate.getBis();
			} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGENALLE)) {
				von = lRueckstandszeitraum.getVon();
				bis = lRueckstandszeitraum.getBis();
				if (lIntervall3Monate.getBis() != null && bis != null && bis.after(lIntervall3Monate.getBis())) {
					bis = lIntervall3Monate.getBis();
				}
			} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGMANUELL)) {
				von = lIntervall3Monate.getVon();
				bis = lIntervall3Monate.getBis();
			}
		}

		return new Intervall(von, bis);
	}

	public static BigDecimal ermittleBetragNachBetragsart(Context pContext, InsolvenzgeldAntragModel pAntrag) {
		BigDecimal result = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

		if (pAntrag != null && !pAntrag.isRueckstaendigeBeitraegeBetragAuswahlNull()) {
			InsoGeldBetragAuswahlType betragsart = pAntrag.getRueckstaendigeBeitraegeBetragAuswahl();

			if (betragsart != null) {
				if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGARBEITNEHMER)) {
					result = pAntrag.getRueckstaendigeBeitraegeArbeitnehmer();
				} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGEN)) {
					result = pAntrag.getRueckstaendigeBeitraegeForderungen();
				} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGARBEITNEHMERFORDERUNGEN)) {
					BigDecimal anBeitrag = pAntrag.isRueckstaendigeBeitraegeArbeitnehmerNull() ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : pAntrag.getRueckstaendigeBeitraegeArbeitnehmer();
					BigDecimal fordBeitrag = pAntrag.isRueckstaendigeBeitraegeForderungenNull() ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : pAntrag.getRueckstaendigeBeitraegeForderungen();
					result = DecimalUtil.add(anBeitrag, fordBeitrag);
				} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGEN3MONATE)) {
					InsolvenzService lInsolvenzService = ServiceFactory.getInstance(InsolvenzService.class);
					Intervall lRueckstandszeitraum = ermittleRueckstandszeitraum(pContext, pAntrag);
					Intervall lIntervall3Monate = lInsolvenzService.ermittleArbeitnehmerRueckstandszeitraum(lInsolvenzService.getDatumForderungsermittlung(pAntrag.getInsolvenzgeld().getInsolvenz()),
							DateUtil.newMinValue(), DateUtil.newMaxValue(), InsolvenzService.ZUORDNUNG_INSOLVENZGELD, lRueckstandszeitraum.getVon());
					result = calculateGesamtRueckstandNachZeitraum(pContext, pAntrag, lIntervall3Monate);
				} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGFORDERUNGENALLE)) {
					result = calculateGesamtRueckstand(pContext, pAntrag);
				} else if (betragsart.equals(InsoGeldBetragAuswahlType.BETRAGMANUELL)) {
					result = pAntrag.getRueckstaendigeBeitraegeInso();
				}
			}
		}
		return result;
	}

	public static RueckstandViewModel getRueckstand(Context pContext, InsolvenzgeldModel pInsolvenzgeld, KontoBewegungModel kb, Integer anzAn, Integer anzAnKinderlos, RueckstandViewModel rsSumme,
			Map<Class<? extends GrundlagenModel>, GrundlagenModel> pGrundlagen, BigDecimal pOffenerBetrag, Date pRueckstaendeAb, Date pRueckstaendeBis, boolean reduzieren) {
		RueckstandViewModel rs = null;

		// Nur Kontobewegungen mit offenem Betrag und Belegart vom Typ AbBeitragGsv
		if (kb != null && isValidValue(kb.getBetrag()) && (kb.isStorniertNull() || !kb.isStorniert())) {
			ForderungVerbindlichkeitModel fv = kb.getForderungVerbindlichkeit();
			if (fv != null) {
				GesetzlicheGrundlagenModel gesetzlicheGrundlage = null;
				if (pGrundlagen != null) {
					gesetzlicheGrundlage = retrieve(GesetzlicheGrundlagenModel.class, pGrundlagen);
				}

				BigDecimal anteil = HUNDERT;
				BigDecimal offenerBetrag = pOffenerBetrag != null ? pOffenerBetrag : fv.getKontobewegung().getOffenerBetrag();
				BigDecimal relevanterBetrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

				// Nur für bestimmte Belegarten nach Teilmonaten berechnen
				if (reduzieren) {
					relevanterBetrag = getBetragNachZeitraum(offenerBetrag, kb.getBezugVon(), kb.getBezugBis(), pRueckstaendeAb, pRueckstaendeBis);
					BigDecimal relevanterGesamtBetrag = getBetragNachZeitraum(kb.getBetrag(), kb.getBezugVon(), kb.getBezugBis(), pRueckstaendeAb, pRueckstaendeBis);
					// Anteil des relevanten Gesamtbetrages zum Betrag der Kontobewegung festlegen
					if (relevanterGesamtBetrag.compareTo(kb.getBetrag()) != 0) {
						anteil = relevanterGesamtBetrag.multiply(HUNDERT);
						anteil = DecimalUtil.divide(anteil, kb.getBetrag());
					}
				} else {
					relevanterBetrag = offenerBetrag;
				}

				rs = getNewRueckstandModel(pContext);
				rs.setKontobewegung(kb);
				rs.setBetragOffen(SollHabenKennzeichenType.HABEN.equals(kb.getSollHabenKennung()) ? offenerBetrag.negate() : offenerBetrag);
				rs.setBetragErmittelt(SollHabenKennzeichenType.HABEN.equals(kb.getSollHabenKennung()) ? relevanterBetrag.negate() : relevanterBetrag);

				for (KontoBewegungPositionModel kbp : kb.getKontoBewegungPosition()) {
					boolean relevant = false;
					BelegPositionsArtType belegPosArt = kbp.getBelegPositionsArt();
					// Reale Ausgleichsbetäge der Position berücksichtigen
					BigDecimal ausgleichsummeGesamt = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					BigDecimal ausgleichsummeGeldfluss = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					BigDecimal ausgleichsummeRest = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					BigDecimal summeListeE = ServiceFactory.getInstance(InsolvenzService.class).getBetragAbsetzungListeE(kb, belegPosArt);
					Collection<AusgleichTeilBetragModel> teilbetraege = kbp.getAusgleichTeilBetrag();
					for (AusgleichTeilBetragModel teilbetrag : teilbetraege) {
						AusgleichModel ausgleich = teilbetrag.getAusgleich();
						BigDecimal teilbetragWert = teilbetrag.getBetrag();
						if (ausgleich.isGueltig()) {
							ausgleichsummeGesamt = DecimalUtil.add(ausgleichsummeGesamt, teilbetragWert);
							if (ausgleich.getKontobewegungHaben() != null) {
								if (ausgleich.getKontobewegungHaben().getTyp().equals(KontoBewegungsTypType.GELDFLUSS)) {
									ausgleichsummeGeldfluss = DecimalUtil.add(ausgleichsummeGeldfluss, teilbetragWert);
								} else if (ausgleich.getKontobewegungHaben().getTyp().equals(KontoBewegungsTypType.FORDERUNGVERBINDLICHKEIT)) {
									ausgleichsummeRest = DecimalUtil.add(ausgleichsummeRest, teilbetragWert);
								} else if (ausgleich.getKontobewegungHaben().getTyp().equals(KontoBewegungsTypType.VERWALTUNG)) {
									if (!BERUECKSICHTIGTE_LISTEKONTEN.contains(ausgleich.getKontobewegungHaben().getZVKonto().getKontoArt())) {
										ausgleichsummeRest = DecimalUtil.add(ausgleichsummeRest, teilbetragWert);
									}
								}
							}
						}
					}
					// Ist mehr per Geldfluss ausgeglichen worden, als auf Liste E abgesetzt,
					// So darf nur der Betrag auf Liste E komplett abgezogen werden. 
					// Der Rest verteilt sich auf gleichmäßig auf den gesamten Monat

					if (summeListeE.compareTo(ausgleichsummeGeldfluss) < 0) {
						BigDecimal wert = DecimalUtil.substract(ausgleichsummeGeldfluss, summeListeE);
						ausgleichsummeGeldfluss = DecimalUtil.substract(ausgleichsummeGeldfluss, wert);
						ausgleichsummeRest = DecimalUtil.add(ausgleichsummeRest, wert);
					} else if (summeListeE.compareTo(ZahlungsverkehrConstant.ZEROBIGDECIMAL) == 0) {
						// Wenn keine Liste E-Buchung, wird nur der Wert in ausgleichsummeRest berücksichtigt 
						ausgleichsummeRest = DecimalUtil.add(ausgleichsummeRest, ausgleichsummeGeldfluss);
						ausgleichsummeGeldfluss = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					}

					if (belegPosArt != null) {
						BigDecimal betrag = getBetragsanteil(DecimalUtil.substract(kbp.getBetrag(), ausgleichsummeRest), anteil);
						if (betrag.compareTo(ausgleichsummeGeldfluss) <= 0) {
							// Ist der restliche Betrag größer als die auf Liste E abgesetzten Geldflüsse
							// so ist hier nichts mehr offen!!!
							betrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
						} else {
							// Betrag auf Liste E komplett abziehen
							betrag = DecimalUtil.substract(betrag, ausgleichsummeGeldfluss);

						}
						BigDecimal betragAn = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
						// Negative Werte bei Haben-Buchungen ausweisen
						if (kb.getSollHabenKennung().equals(SollHabenKennzeichenType.HABEN)) {
							betrag = betrag.negate();
							betragAn = betragAn.negate();
						}

						if (BELEGPOSITIONSARTEN_KV_BEITRAG.contains(belegPosArt)) {
							// Beitrag KV Gesamt
							rs.setKv(rs.getKv().add(betrag));
							rsSumme.setKv(rsSumme.getKv().add(betrag));
							relevant = true;
						} else if (BELEGPOSITIONSARTEN_AV_BEITRAG.contains(belegPosArt)) {
							// Beitrag BA Gesamt
							rs.setBa(rs.getBa().add(betrag));
							rsSumme.setBa(rsSumme.getBa().add(betrag));
							relevant = true;
						} else if (BELEGPOSITIONSARTEN_RV_BEITRAG.contains(belegPosArt)) {
							// Beitrag RV Gesamt										
							rs.setRv(rs.getRv().add(betrag));
							rsSumme.setRv(rsSumme.getRv().add(betrag));
							relevant = true;
						} else if (BELEGPOSITIONSARTEN_PV_BEITRAG.contains(belegPosArt)) {
							// Beitrag PV Gesamt
							rs.setPv(rs.getPv().add(betrag));
							rsSumme.setPv(rsSumme.getPv().add(betrag));
							relevant = true;
						} else if (BELEGPOSITIONSARTEN_PFLZB_ZUSATZBEITRAG.contains(belegPosArt)) {
							// PFLZB Arbeitnehmeranteil hier berücksichtigen, da es nur AN-Anteil gibt
							rs.setPflZb(rs.getPflZb().add(betrag));
							rsSumme.setPflZb(rsSumme.getPflZb().add(betrag));
							relevant = true;
						}

						if (relevant) {
							// Summen
							rs.setSumme(rs.getSumme().add(betrag));
							rs.setBetragManuell(rs.getSumme());
							rs.setGesamt(rs.getGesamt().add(betrag));
							rsSumme.setSumme(rsSumme.getSumme().add(betrag));
							rsSumme.setGesamt(rsSumme.getGesamt().add(betrag));
						}
					}
				}

				// Beitrag KV Arbeitnehmeranteil
				if (isValidValue(rs.getKv())) {
					GrundlagenSatzungKVModel grundlageKV = null;
					grundlageKV = retrieve(GrundlagenSatzungKVModel.class, pGrundlagen);
					BigDecimal betragAn = getKvAnAnteil(kb, rs.getKv(), grundlageKV);
					if (isValidValue(betragAn)) {
						rs.setKvAn(betragAn);
						rsSumme.setKvAn(rsSumme.getKvAn().add(betragAn));
						rs.setSummeAn(rs.getSummeAn().add(betragAn));
						rsSumme.setSummeAn(rsSumme.getSummeAn().add(betragAn));
					}
				}

				// Beitrag BA Arbeitnehmeranteil
				if (isValidValue(rs.getBa())) {
					BigDecimal betragAn = getAvAnAnteil(rs.getBa());
					if (isValidValue(betragAn)) {
						rs.setBaAn(betragAn);
						rsSumme.setBaAn(rsSumme.getBaAn().add(betragAn));
						rs.setSummeAn(rs.getSummeAn().add(betragAn));
						rsSumme.setSummeAn(rsSumme.getSummeAn().add(betragAn));
					}
				}

				// Beitrag RV Arbeitnehmeranteil
				if (isValidValue(rs.getRv())) {
					BigDecimal betragAn = getRvAnAnteil(rs.getRv());
					if (isValidValue(betragAn)) {
						rs.setRvAn(betragAn);
						rsSumme.setRvAn(rsSumme.getRvAn().add(betragAn));
						rs.setSummeAn(rs.getSummeAn().add(betragAn));
						rsSumme.setSummeAn(rsSumme.getSummeAn().add(betragAn));
					}
				}

				// Beitrag PV Arbeitnehmeranteil	
				if (isValidValue(rs.getPv())) {
					BigDecimal betragAn = getPvAnAnteil(rs.getPv(), anzAn, anzAnKinderlos, gesetzlicheGrundlage);
					if (isValidValue(betragAn)) {
						rs.setPvAn(betragAn);
						rsSumme.setPvAn(rsSumme.getPvAn().add(betragAn));
						rs.setSummeAn(rs.getSummeAn().add(betragAn));
						rsSumme.setSummeAn(rsSumme.getSummeAn().add(betragAn));
					}
				}

				// Beitrag PFLZB (muss nur addiert werden, da reiner AN-Anteil)	
				if (isValidValue(rs.getPflZb())) {
					rs.setPflZbAn(rs.getPflZb());
					rsSumme.setPflZbAn(rsSumme.getPflZbAn().add(rs.getPflZb()));
					rs.setSummeAn(rs.getSummeAn().add(rs.getPflZb()));
					rsSumme.setSummeAn(rsSumme.getSummeAn().add(rs.getPflZb()));
				}

				// Nebenforderungen ermitteln
				if (fv.getVollstreckungskostenSollstellung() != null) {
					if (fv.getVollstreckungskostenSollstellung().getAuslage() != null && !VK_STATUS_STORNIERT_GELOESCHT.contains(fv.getVollstreckungskostenSollstellung().getAuslage().getStatus())) {
						if (fv.getVollstreckungskostenSollstellung().getAuslage().getBetrag() != null) {
							rs.setAuslage(fv.getVollstreckungskostenSollstellung().getAuslage().getBetrag());
							rs.setSummeNebenforderungen(rs.getSummeNebenforderungen().add(rs.getAuslage()));
							rs.setGesamt(rs.getGesamt().add(rs.getAuslage()));
							rsSumme.setAuslage(rsSumme.getAuslage().add(rs.getAuslage()));
							rsSumme.setSummeNebenforderungen(rsSumme.getSummeNebenforderungen().add(rs.getAuslage()));
							rsSumme.setGesamt(rsSumme.getGesamt().add(rs.getAuslage()));
						}
					}

					if (fv.getVollstreckungskostenSollstellung().getGebuehren() != null && !VK_STATUS_STORNIERT_GELOESCHT.contains(fv.getVollstreckungskostenSollstellung().getGebuehren().getStatus())) {
						if (fv.getVollstreckungskostenSollstellung().getGebuehren().getBetrag() != null) {
							rs.setGebuehr(fv.getVollstreckungskostenSollstellung().getGebuehren().getBetrag());
							rs.setSummeNebenforderungen(rs.getSummeNebenforderungen().add(rs.getGebuehr()));
							rs.setGesamt(rs.getGesamt().add(rs.getGebuehr()));
							rsSumme.setGebuehr(rsSumme.getGebuehr().add(rs.getGebuehr()));
							rsSumme.setSummeNebenforderungen(rsSumme.getSummeNebenforderungen().add(rs.getGebuehr()));
							rsSumme.setGesamt(rsSumme.getGesamt().add(rs.getGebuehr()));
						}
					}
				}

				if (CollectionUtils.isNotEmpty(fv.getSaeumniszuschlag())) {
					BigDecimal zuschlaege = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					for (SaeumniszuschlagModel sz : fv.getSaeumniszuschlag()) {
						if (sz != null && isValidValue(sz.getBetrag())) {
							zuschlaege = zuschlaege.add(sz.getBetrag());
						}
					}
					rs.setZuschlag(zuschlaege);
					rs.setSummeNebenforderungen(rs.getSummeNebenforderungen().add(zuschlaege));
					rs.setGesamt(rs.getGesamt().add(zuschlaege));
					rsSumme.setZuschlag(rsSumme.getZuschlag().add(zuschlaege));
					rsSumme.setSummeNebenforderungen(rsSumme.getSummeNebenforderungen().add(zuschlaege));
					rsSumme.setGesamt(rsSumme.getGesamt().add(zuschlaege));
				}
				setRueckstandManuell(rs, pInsolvenzgeld);
			}
		}
		return rs;
	}

	private static BigDecimal getKvAnAnteil(KontoBewegungModel kb, BigDecimal pBetrag, GrundlagenSatzungKVModel pGrundlageKV) {
		Assert.isNotNull(kb.getBezugVon(), "Bezugsdatum der KB " + kb.getBelegnummer() + " nicht gesetzt.");

		BigDecimal lResult = null;
		if (isValidValue(pBetrag)) {
			if (kb.getBezugVon().before(InsolvenzService.EINFUEHRUNG_ZUSATZBEITRAG_2015)) {
				if (pGrundlageKV != null && pGrundlageKV.getBeitragsSatzKVAllgemein() != null) {
					BigDecimal prozentAnKv = DecimalUtil.substract(pGrundlageKV.getBeitragsSatzKVAllgemein(), InsolvenzService.KV_ARBEITNEHMER_ZUSCHLAG_VOR_EINFUEHRUNG_ZUSATZBEITRAG_2015);
					prozentAnKv = DecimalUtil.divide(prozentAnKv, ZWEI);
					final BigDecimal beitragssatzAnKv = DecimalUtil.add(prozentAnKv, InsolvenzService.KV_ARBEITNEHMER_ZUSCHLAG_VOR_EINFUEHRUNG_ZUSATZBEITRAG_2015);
					lResult = DecimalUtil.roundFinalMoney(DecimalUtil.divide(DecimalUtil.multiply(pBetrag, beitragssatzAnKv), pGrundlageKV.getBeitragsSatzKVAllgemein()));
				}
			} else {
				BigDecimal kvAn = pBetrag.divide(ZWEI);
				lResult = DecimalUtil.roundFinalMoney(kvAn);
			}
		}
		return lResult;
	}

	private static BigDecimal getRvAnAnteil(BigDecimal pBetrag) {
		BigDecimal lResult = null;
		if (isValidValue(pBetrag)) {
			BigDecimal lBetrag = pBetrag.divide(ZWEI);
			lResult = DecimalUtil.roundFinalMoney(lBetrag);
		}

		return lResult;
	}

	private static BigDecimal getAvAnAnteil(BigDecimal pBetrag) {
		BigDecimal lResult = null;
		if (isValidValue(pBetrag)) {
			BigDecimal lBetrag = pBetrag.divide(ZWEI);
			lResult = DecimalUtil.roundFinalMoney(lBetrag);
		}

		return lResult;
	}

	private static BigDecimal getPvAnAnteil(BigDecimal pBetrag, Integer anzahlArbeitnehmer, Integer anzahlArbeitnehmerKinderlos, GesetzlicheGrundlagenModel pGesetzlicheGrundlage) {
		BigDecimal lResult = null;
		if (pGesetzlicheGrundlage != null && isValidValue(pBetrag)) {
			BigDecimal lBeitragssatzAnPv = null;
			BigDecimal lBeitragssatzAnPvKinderlos = null;
			if (pGesetzlicheGrundlage != null && pGesetzlicheGrundlage.getBeitragssatzPV() != null) {
				lBeitragssatzAnPv = DecimalUtil.divide(pGesetzlicheGrundlage.getBeitragssatzPV(), ZWEI);
			}

			if (isValidValue(lBeitragssatzAnPv)) {
				if (anzahlArbeitnehmer != null && anzahlArbeitnehmerKinderlos != null) {
					if (isValidValue(pGesetzlicheGrundlage.getBeitragssatzPVKinderlosZuschlag())) {
						lBeitragssatzAnPvKinderlos = DecimalUtil.add(lBeitragssatzAnPv, pGesetzlicheGrundlage.getBeitragssatzPVKinderlosZuschlag());
					} else {
						lBeitragssatzAnPvKinderlos = pGesetzlicheGrundlage.getBeitragssatzPV();
					}

					BigDecimal betragPv = DecimalUtil.multiply(DecimalUtil.divide(pBetrag, new BigDecimal(anzahlArbeitnehmer)), new BigDecimal(anzahlArbeitnehmer - anzahlArbeitnehmerKinderlos));
					BigDecimal betragPvKinderlos = DecimalUtil.multiply(DecimalUtil.divide(pBetrag, new BigDecimal(anzahlArbeitnehmer)), new BigDecimal(anzahlArbeitnehmerKinderlos));
					lResult = DecimalUtil.roundFinalMoney(DecimalUtil.add((DecimalUtil.divide(DecimalUtil.multiply(betragPv, lBeitragssatzAnPv), pGesetzlicheGrundlage.getBeitragssatzPV())),
							(DecimalUtil.divide(DecimalUtil.multiply(betragPvKinderlos, lBeitragssatzAnPvKinderlos), pGesetzlicheGrundlage.getBeitragssatzPV()))));
				} else {
					lResult = DecimalUtil.roundFinalMoney(DecimalUtil.divide(DecimalUtil.multiply(pBetrag, lBeitragssatzAnPv), pGesetzlicheGrundlage.getBeitragssatzPV()));
				}
			}
		}
		return lResult;
	}

	public static Collection<ZeitraumViewModel> ermittleFehlendeSollstellungsZeiten(Context pContext, ZahlungsverkehrKontoModel pZVKonto) {
		Collection<ZeitraumViewModel> result = new ArrayList<>();
		
		if (pZVKonto != null && pZVKonto.getKontoinhaberOderNull() != null && pZVKonto.getKontoinhaberOderNull() instanceof BetriebsstaetteModel) {
			TemporalModel letzteMeldezeit = ServiceFactory.getInstance(BeitraegeInterface.class).ermittelLetzteMeldezeit(pContext, (BetriebsstaetteModel)pZVKonto.getKontoinhaberOderNull());
			if (letzteMeldezeit != null) {
				Date ultimo = DateUtil.getLastDayOfMonth(DateUtil.subtractMonth(DateUtil.newToday(), 1));
				Date meldezeitVon = letzteMeldezeit.getValidFrom();
				Date meldezeitBis = letzteMeldezeit.getValidTo();
				if (meldezeitBis.compareTo(ultimo) > 0) {
					meldezeitBis = ultimo;
				}
				Collection<KontoBewegungModel> beitraege = ServiceFactory.getInstance(InsolvenzService.class).sucheBeitragsforderungenNachBezugszeitraum(pContext, pZVKonto, meldezeitVon, meldezeitBis);
				Date von = meldezeitVon;
				if (beitraege.size() > 0) {
					for (KontoBewegungModel beitrag : beitraege) {
						Date bezugVon = beitrag.getBezugVon();
						Date bezugBis = beitrag.getBezugBis();
						if (bezugVon.compareTo(von) > 0) {
							result.add(new ZeitraumViewModel(von, DateUtil.subtract(bezugVon, 1)));
						} 
						if (von.compareTo(bezugBis) < 0) {
							von = DateUtil.add(bezugBis, 1);
						}	
					}
					// Letzte Zeit prüfen
					if (von.compareTo(meldezeitBis) < 0) {
						result.add(new ZeitraumViewModel(von, meldezeitBis));
					}
				} else {
					result.add(new ZeitraumViewModel(meldezeitVon, meldezeitBis));
				}
			}
		}
		return result;
	}
	
	public static Lohndaten ermittleLohndaten(Context pContext, LohnabrechnungModel pLohnabrechnung, boolean pHatKinder) {
		LohnabrechnungService lohnabrechnungService = ServiceFactory.getInstance(LohnabrechnungService.class);
		Lohndaten lohndaten = null;

		if (pLohnabrechnung != null) {
			Map<Class<? extends GrundlagenModel>, GrundlagenModel> grundlagen = getGrundlagen(pContext, pLohnabrechnung.getZeitraumVon(), pLohnabrechnung.getZeitraumBis());

			BigDecimal beitragsbemessungsgrenzeKV = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragsbemessungsgrenzeRV = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

			BigDecimal beitragssatzPvKinderlos = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragssatzKVAllgemein = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragssatzKVZusatzbeitrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragssatzRV = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragssatzAV = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
			BigDecimal beitragssatzPV = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

			// Berechnungsgrundlagen ermitteln
			if (grundlagen != null) {
				GrundlagenSatzungKVModel grundlageKV = retrieve(GrundlagenSatzungKVModel.class, grundlagen);
				if (grundlageKV != null) {
					if (isValidValue(grundlageKV.getBeitragsSatzKVAllgemein())) {
						beitragssatzKVAllgemein = grundlageKV.getBeitragsSatzKVAllgemein();
					}
					if (isValidValue(grundlageKV.getBeitragssatzZusatzProzent())) {
						beitragssatzKVZusatzbeitrag = grundlageKV.getBeitragssatzZusatzProzent();
					}
				}

				// Beitragsbemessungsgrenze ermitteln
				GesetzlicheGrundlagenModel grundlageAllgemein = retrieve(GesetzlicheGrundlagenModel.class, grundlagen);
				if (grundlageKV != null && grundlageAllgemein != null) {
					if (pLohnabrechnung.getArbeitnehmerZeit() != null && pLohnabrechnung.getArbeitnehmerZeit().getInsolvenzgeld() != null) {
						beitragsbemessungsgrenzeKV = grundlageAllgemein.getBeitragsbemessungsgrenzeMonatlichKV();
						if (((BetriebsstaetteModel) pLohnabrechnung.getArbeitnehmerZeit().getInsolvenzgeld().getInsolvenz().getSchuldner()).getRechtskreis().equals(RechtskreisType.WEST)) {
							beitragsbemessungsgrenzeRV = grundlageAllgemein.getBeitragsbemessungsgrenzeMonatlichRVWest();
						} else {
							beitragsbemessungsgrenzeRV = grundlageAllgemein.getBeitragsbemessungsgrenzeMonatlichRVOst();
						}
					}
					//PV
					if (isValidValue(grundlageAllgemein.getBeitragssatzPV())) {
						beitragssatzPV = grundlageAllgemein.getBeitragssatzPV();

						if (isValidValue(grundlageAllgemein.getBeitragssatzPVKinderlosZuschlag())) {
							beitragssatzPvKinderlos = grundlageAllgemein.getBeitragssatzPVKinderlosZuschlag();
						}
					}
					// RV
					if (isValidValue(grundlageAllgemein.getBeitragssatzRV())) {
						beitragssatzRV = grundlageAllgemein.getBeitragssatzRV();
					}

					// AV
					if (isValidValue(grundlageAllgemein.getBeitragssatzAV())) {
						beitragssatzAV = grundlageAllgemein.getBeitragssatzAV();
					}
				}
			}
			GesetzlicheGrundlagen gesetzlicheGrundlagen = new GesetzlicheGrundlagen(beitragssatzKVAllgemein, beitragssatzKVZusatzbeitrag, beitragssatzRV, beitragssatzAV, beitragssatzPV,
					beitragssatzPvKinderlos);
			Beitragsbemessungsgrenzen beitragsbemessungsgrenzen = new Beitragsbemessungsgrenzen(beitragsbemessungsgrenzeKV, beitragsbemessungsgrenzeRV);
			lohndaten = new Lohndaten(pLohnabrechnung.getZeitraumVon(), pLohnabrechnung.getBruttoentgelt(),
					pLohnabrechnung.getBruttoentgelt() == null ? pLohnabrechnung.getSvBetrag() : null, new BigDecimal(pLohnabrechnung.getSvTage()), pLohnabrechnung.isLetzteZeile(), pHatKinder);
			lohndaten = lohnabrechnungService.berechneLohndaten(gesetzlicheGrundlagen, beitragsbemessungsgrenzen, lohndaten);

		}
		return lohndaten;
	}

	private static RueckstandViewModel getNewRueckstandModel(Context pContext) {
		RueckstandViewModel result = new RueckstandViewModel(pContext);
		result.setAuslage(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setBa(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setBaAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setGebuehr(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setGesamt(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setKv(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setKvAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setPflZb(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setPflZbAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setPv(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setPvAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setRv(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setRvAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setSumme(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setSummeAn(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setSummeNebenforderungen(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setZuschlag(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		result.setBetragManuell(ZahlungsverkehrConstant.ZEROBIGDECIMAL);
		return result;
	}

	private static BigDecimal getBetragsanteil(BigDecimal pBetrag, BigDecimal pAnteil) {
		BigDecimal result = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

		if (pBetrag != null && pAnteil != null) {
			if (pAnteil.equals(HUNDERT)) {
				result = pBetrag;
			} else {
				result = DecimalUtil.multiply(pBetrag, pAnteil);
				result = DecimalUtil.divide(result, new BigDecimal("100.00"));
				result = DecimalUtil.roundFinalMoney(result);
			}
		}
		return result;
	}

	private static boolean isValidValue(BigDecimal pVal) {

		if (pVal == null || pVal.compareTo(ZahlungsverkehrConstant.ZEROBIGDECIMAL) == 0) {
			return false;
		}

		return true;
	}

	// PCR 131582: Fällt der Bezugszeitraum nicht vollständig in den Rückstandszeitraum, so muss auch hier
	// der Betrag um den Anteil reduziert werden, der nicht in den Rückstandszeitraum fällt.	
	private static BigDecimal getBetragNachZeitraum(BigDecimal pBetrag, Date pBezugVon, Date pBezugBis, Date pRueckstandVon, Date pRueckstandBis) {
		BigDecimal result = pBetrag;

		if (isValidValue(pBetrag) && pBezugVon != null && pBezugBis != null && pRueckstandVon != null && pRueckstandBis != null) {
			if (pBezugVon.before(pRueckstandVon) || pBezugBis.after(pRueckstandBis)) {
				result = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
				for (MonatsBeitragModel monatsBeitrag : getBezugszeitraumListe(pBezugVon, pBezugBis, pBetrag)) {
					BigDecimal anteil = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
					if (DateUtil.isOverlapping(pRueckstandVon, pRueckstandBis, monatsBeitrag.getBezugVon(), monatsBeitrag.getBezugBis())) {
						if (monatsBeitrag.getBezugVon().before(pRueckstandVon) && !monatsBeitrag.getBezugBis().after(pRueckstandBis)) {
							//4a. Der Bezugsmonat beginnt vor dem Rückstandszeitraum und endet im Rückstandszeitraum
							//    (BezugsmonatVon < "Rückstände von" und BezugsmonatBis <= "Rückstände bis")
							//    Anteil = (UlitmoDesMonats - Tag aus "Rückstände von" + 1) * BetragJeMonat
							Date bezugVon = monatsBeitrag.getBezugVon();
							Date ultimo = DateUtil.newDate(DateUtil.getYear(bezugVon), DateUtil.getMonth(bezugVon) + 1, 1);
							ultimo = DateUtil.subtract(ultimo, EINS);
							int anzTage = DateUtil.getDay(ultimo) - DateUtil.getDay(pRueckstandVon) + EINS;
							anteil = DecimalUtil.divide(new BigDecimal(anzTage).multiply(monatsBeitrag.getBetrag()), DREISSIG);
						} else if (monatsBeitrag.getBezugVon().before(pRueckstandVon) && monatsBeitrag.getBezugBis().after(pRueckstandBis)) {
							//4b. Der Bezugsmonat beginnt vor dem Rückstandszeitraum und endet nach dem Rückstandszeitraum, d. h. der Rückstandszeitraum ist kleiner einem Monat
							//    (BezugsmonatVon < "Rückstände von" und BezugsmonatBis > "Rückstände bis")								
							//    Anteil = Anzahl Tage im Rückstandszeitraum * BetragJeMonat / 30
							anteil = DecimalUtil.divide(new BigDecimal(DateUtil.datePeriod(pRueckstandBis, pRueckstandVon) + EINS).multiply(monatsBeitrag.getBetrag()), DREISSIG);
						} else if (!monatsBeitrag.getBezugVon().before(pRueckstandVon) && !monatsBeitrag.getBezugBis().after(pRueckstandBis)) {
							//4c. Der Bezugsmonat ist komplett im Rückstandszeitraum enthalten
							//    (BezugsmonatVon >= "Rückstände von" und BezugsmonatBis <= "Rückstände bis")             								
							//    Anteil = BetragJeMonat
							anteil = monatsBeitrag.getBetrag();
						} else if (!monatsBeitrag.getBezugVon().before(pRueckstandVon) && !monatsBeitrag.getBezugVon().after(pRueckstandBis) && monatsBeitrag.getBezugBis().after(pRueckstandBis)) {
							//4d. Der Bezugsmonat endet nach dem Rückstandszeitraum 
							//    (BezugsmonatVon >= "Rückstände von" und BezugsmonatBis > "Rückstände bis")
							//    Anteil = Tag aus "Rückstände bis" * BetragJeMonat / 30
							anteil = DecimalUtil.divide(new BigDecimal(DateUtil.getDay(pRueckstandBis)).multiply(monatsBeitrag.getBetrag()), DREISSIG);
						}
					}
					result = result.add(anteil);
				}
				result = DecimalUtil.roundFinalMoney(result);
			}
		}

		return result;
	}

	private static Collection<MonatsBeitragModel> getBezugszeitraumListe(Date pBezugVon, Date pBezugBis, BigDecimal pBetrag) {
		Collection<MonatsBeitragModel> result = new ArrayList<>();
		final Calendar lBezugszeitraumAnfang = new GregorianCalendar();
		final Calendar lBezugszeitraumEnde = new GregorianCalendar();
		int anzTageBei30TageMonat = 0;

		// Bezugszeitraum <= einen Monat
		if (DateUtil.getMonth(pBezugVon) == DateUtil.getMonth(pBezugBis) && DateUtil.getYear(pBezugVon) == DateUtil.getYear(pBezugBis)) {
			MonatsBeitragModel bezugsmonat = new MonatsBeitragModel();
			bezugsmonat.setBezugVon(pBezugVon);
			bezugsmonat.setBezugBis(pBezugBis);
			bezugsmonat.setBetrag(pBetrag);
			bezugsmonat.setAnzahlTageImBankjahr(DateUtil.datePeriod(pBezugBis, pBezugVon));
			anzTageBei30TageMonat = bezugsmonat.getAnzahlTageImBankjahr();
			result.add(bezugsmonat);
		} else {
			lBezugszeitraumAnfang.setTime(pBezugVon);
			lBezugszeitraumEnde.setTime(pBezugVon);
			lBezugszeitraumEnde.set(Calendar.DAY_OF_MONTH, lBezugszeitraumEnde.getActualMaximum(Calendar.DAY_OF_MONTH));

			while (!lBezugszeitraumAnfang.getTime().after(pBezugBis)) {
				MonatsBeitragModel bezugsmonat = new MonatsBeitragModel();
				bezugsmonat.setBezugVon(lBezugszeitraumAnfang.getTime());
				bezugsmonat.setBezugBis(lBezugszeitraumEnde.getTime());
				int anzTage = DateUtil.datePeriod(lBezugszeitraumAnfang.getTime(), lBezugszeitraumEnde.getTime());
				if (lBezugszeitraumAnfang.get(Calendar.DAY_OF_MONTH) == 1 && lBezugszeitraumEnde.getActualMaximum(Calendar.DAY_OF_MONTH) == lBezugszeitraumEnde.get(Calendar.DAY_OF_MONTH)) {
					anzTage = 30;
				} else if (lBezugszeitraumAnfang.get(Calendar.DAY_OF_MONTH) == 1 && !(lBezugszeitraumEnde.getActualMaximum(Calendar.DAY_OF_MONTH) == lBezugszeitraumEnde.get(Calendar.DAY_OF_MONTH))) {
					anzTage += 1;
				}
				anzTageBei30TageMonat += anzTage;
				bezugsmonat.setAnzahlTageImBankjahr(anzTage);
				result.add(bezugsmonat);

				lBezugszeitraumAnfang.add(Calendar.MONTH, 1);
				lBezugszeitraumAnfang.set(Calendar.DAY_OF_MONTH, 1);
				lBezugszeitraumEnde.setTime(lBezugszeitraumAnfang.getTime());
				lBezugszeitraumEnde.set(Calendar.DAY_OF_MONTH, lBezugszeitraumEnde.getActualMaximum(Calendar.DAY_OF_MONTH));

				// Letzter Datensatz
				if (lBezugszeitraumEnde.getTime().after(pBezugBis)) {
					lBezugszeitraumEnde.setTime(pBezugBis);
				}
			}
		}

		int anzMonate = result.size();
		if (anzMonate > 1 && anzTageBei30TageMonat > 0 && isValidValue(pBetrag)) {
			BigDecimal betragJeMonat = pBetrag.divide(new BigDecimal(anzMonate));
			BigDecimal betragJeTag = DecimalUtil.divide(pBetrag, new BigDecimal(anzTageBei30TageMonat));
			for (MonatsBeitragModel monatsBeitrag : result) {
				// Betrag je Monat ermitteln		
				if (DateUtil.getDay(pBezugVon) != 1 || DateUtil.getDay(pBezugBis) != lBezugszeitraumEnde.getActualMaximum(Calendar.DAY_OF_MONTH)) {
					monatsBeitrag.setBetrag(betragJeTag.multiply(new BigDecimal(monatsBeitrag.getAnzahlTageImBankjahr())));
				} else {
					monatsBeitrag.setBetrag(betragJeMonat);
				}
			}
		}
		return result;
	}

	private static Map<Class<? extends GrundlagenModel>, GrundlagenModel> getGrundlagen(Context pContext, Date pZeitraumVon, Date pZeitraumBis) {
		Map<Class<? extends GrundlagenModel>, GrundlagenModel> result = new HashMap<>();
		ProduktInterface lInterface = ServiceFactory.getInstance(ProduktInterface.class);

		GrundlagenSatzungKVSearchModel kvSearchModel = new GrundlagenSatzungKVSearchModel(pContext);
		kvSearchModel.setValidFrom("<= " + DateUtil.getDate(pZeitraumVon));
		kvSearchModel.setValidTo(">= " + DateUtil.getDate(pZeitraumBis));

		FinderResult<GrundlagenSatzungKVModel> kvResult = lInterface.findGrundlagenSatzungKV(pContext, kvSearchModel);
		for (GrundlagenSatzungKVModel satzungKV : kvResult.getSearchResult()) {
			if (StringUtils.isEmpty(satzungKV.getBetriebsnummer())) {
				result.put(GrundlagenSatzungKVModel.class, satzungKV);
				break;
			}
		}

		GesetzlicheGrundlagenSearchModel ggSearchModel = new GesetzlicheGrundlagenSearchModel(pContext);
		ggSearchModel.setValidFrom("<= " + DateUtil.getDate(pZeitraumVon));
		ggSearchModel.setValidTo(">= " + DateUtil.getDate(pZeitraumBis));

		FinderResult<GesetzlicheGrundlagenModel> ggResult = lInterface.findGesetzlicheGrundlagen(pContext, ggSearchModel);
		for (GesetzlicheGrundlagenModel lGesetzlicheGrundlagen : ggResult.getSearchResult()) {
			result.put(GesetzlicheGrundlagenModel.class, lGesetzlicheGrundlagen);
			break;
		}
		return result;
	}

	public static boolean validateBetragManuell(Context pContext, RueckstandViewModel pRueckstand) {
		if (pRueckstand.getBetragManuell() == null || pRueckstand.getBetragManuell().signum() < 0) {
			pContext.postEvent(ForderungsmanagementMessageCodesEnum.FRM0312.name(), null);
			return false;
		}
		return true;
	}

	public static BigDecimal calculateGesamtManuell(Context pContext, Collection<RueckstandViewModel> pRueckstaende) {
		BigDecimal lGesamtManuell = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
		for (RueckstandViewModel lRueckstand : pRueckstaende) {
			if (lRueckstand.getBetragManuell() != null) {
				lGesamtManuell = DecimalUtil.add(lGesamtManuell, lRueckstand.getBetragManuell());
			}
		}
		return lGesamtManuell;
	}

	public static BigDecimal calculateGesamtRueckstand(Context pContext, InsolvenzgeldAntragModel pInsolvenzgeldAntrag) {
		InsolvenzService lInsolvenzService = ServiceFactory.getInstance(InsolvenzService.class);
		Intervall lRueckstandszeitraum = ermittleRueckstandszeitraum(pContext, pInsolvenzgeldAntrag);
		Intervall lIntervall3Monate = lInsolvenzService.ermittleArbeitnehmerRueckstandszeitraum(lInsolvenzService.getDatumForderungsermittlung(pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz()),
				DateUtil.newMinValue(), DateUtil.newMaxValue(), InsolvenzService.ZUORDNUNG_INSOLVENZGELD, lRueckstandszeitraum.getVon());
		
		return InsolvenzPaUtil.calculateGesamtRueckstandNachZeitraum(pContext, pInsolvenzgeldAntrag, new Intervall(DateUtil.newMinValue(), lIntervall3Monate.getBis()));
	}

	public static BigDecimal calculateGesamtRueckstandNachZeitraum(Context pContext, InsolvenzgeldAntragModel pInsolvenzgeldAntrag, Intervall pIntervall) {
		BigDecimal lRueckstandBetrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

		if (pInsolvenzgeldAntrag.getInsolvenzgeld() != null && pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz() != null) {
			Collection<RueckstandViewModel> lRueckstaende = ermittleRueckstaende(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz(), pIntervall.getVon(), pIntervall.getBis(),
					pInsolvenzgeldAntrag);
			for (RueckstandViewModel lRueckstand : lRueckstaende) {
				if (lRueckstand.getKontobewegung() == null) {
					lRueckstandBetrag = lRueckstand.getSumme();
					break;
				}
			}
		}
		return lRueckstandBetrag;
	}

	public static boolean isHatKinder(VersichertePersonModel pVersichertePerson, Intervall pIntervall) {
		Assert.isNotNull(pVersichertePerson, "pVersichertePerson is null!");
		Assert.isNotNull(pIntervall, "pIntervall is null!");

		boolean lHatKinder = false;
		for (VersichertenMerkmalModel lVersichertenMerkmal : pVersichertePerson.getVersichertenMerkmal()) {
			if (!lVersichertenMerkmal.isHatKinderNull() && lVersichertenMerkmal.isHatKinder()
					&& DateUtil.isOverlapping(lVersichertenMerkmal.getValidFrom(), lVersichertenMerkmal.getValidTo(), pIntervall.getVon(), pIntervall.getBis())) {
				lHatKinder = true;
				break;
			}
		}
		return lHatKinder;
	}

	public static Collection<ArbeitnehmerZeitModel> ermittleZugeordneteArbeitnehmer(int pZuordnung, InsolvenzModel pInsolvenz) {
		Assert.isNotNull(pInsolvenz, "pInsolvenz is null!");

		Collection<ArbeitnehmerZeitModel> lZugeordneteArbeitnehmer = Collections.emptySet();
		if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZ) {
			lZugeordneteArbeitnehmer = pInsolvenz.getArbeitnehmerZeit();
		} else if (pZuordnung == InsolvenzService.ZUORDNUNG_BETRIEBSPRUEFUNG && pInsolvenz.getBetriebspruefung() != null) {
			lZugeordneteArbeitnehmer = pInsolvenz.getBetriebspruefung().getArbeitnehmerZeit();
		} else if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD && pInsolvenz.getInsolvenzgeld() != null) {
			lZugeordneteArbeitnehmer = pInsolvenz.getInsolvenzgeld().getArbeitnehmerZeit();
		}
		return lZugeordneteArbeitnehmer;
	}

	@SuppressWarnings("unchecked")
	public static Collection<VersicherteViewModel> ermittleArbeitnehmer(final Context pContext, final InsolvenzModel pInsolvenz, final boolean pNurZugeordneteAnzeigen, final int pZuordnung,
			final Intervall pReferenzZeitraum) {
		Set<VersicherteViewModel> lErmittelteArbeitnehmer = new HashSet<>();

		if (pInsolvenz != null && pInsolvenz.getZvKonto() != null && pInsolvenz.getZvKonto().getKontoinhaberPartnerRolle() != null) {
			BetriebsstaetteModel lArbeitgeber = null;
			if (pInsolvenz.getZvKonto().getKontoinhaberPartnerRolle().getPartner() instanceof BetriebsstaetteModel
					&& StringUtils.isNotEmpty((lArbeitgeber = (BetriebsstaetteModel) pInsolvenz.getZvKonto().getKontoinhaberPartnerRolle().getPartner()).getBetriebsnummer())) {
				Collection<ArbeitnehmerZeitModel> lZugeordneteArbeitnehmer = ermittleZugeordneteArbeitnehmer(pZuordnung, pInsolvenz);
				// Nur Starten, wenn entweder schon Arbeitnehmer zugeordnet wurden oder auch nicht zugeordnete angezeigt werden sollen
				// Sonst ist das Ergebnis eh eine leere Menge
				boolean lErsterAufruf = pNurZugeordneteAnzeigen && CollectionUtils.isEmpty(lZugeordneteArbeitnehmer);
				if (!lErsterAufruf) {
					ScrollableResults<VersicherungsZeitModel> lVersicherungsZeitenZuBetriebsnummer = null;
					try {
						// Alle Betriebsnummern der Hauptstelle ermitteln
						Collection<String> lBetriebsgeflecht = ServiceFactory.getInstance(BeitraegeInterface.class).ermittelBetriebsgeflecht(pContext, lArbeitgeber.getBetriebsnummer(),
								pReferenzZeitraum.getVon(), pReferenzZeitraum.getBis());

						// Für alle - Haupt- und Nebenstellen die Arbeitnehmer ermitteln
						for (String lBetriebsnummer : lBetriebsgeflecht) {
							lVersicherungsZeitenZuBetriebsnummer = MeldungenDiensteFactory.gibDiensteFuerBeitrag().ermittelnVersicherungsZeitenZuBetriebsnummer(pContext, null, null, null, null,
									lBetriebsnummer, pReferenzZeitraum.getVon(), pReferenzZeitraum.getBis(), null, PersonenGruppeType.getAllPersonenGruppeTypes(), false);

							for (VersicherungsZeitModel lVersicherungsZeit : lVersicherungsZeitenZuBetriebsnummer) {
								VersicherteViewModel lVersicherter = createVersicherteView(pContext, lVersicherungsZeit, pZuordnung, pReferenzZeitraum);
								Set<ArbeitnehmerZeitModel> lMatchedArbeitnehmer = new HashSet<>();

								for (ArbeitnehmerZeitModel lZugeordneterArbeitnehmer : lZugeordneteArbeitnehmer) {
									if (lZugeordneterArbeitnehmer.getModelOid() == lVersicherungsZeit.getOid().longValue()) {
										fillVersicherteView(lVersicherter, lZugeordneterArbeitnehmer);
										lMatchedArbeitnehmer.add(lZugeordneterArbeitnehmer);
										break;
									}
								}
								lZugeordneteArbeitnehmer.removeAll(lMatchedArbeitnehmer);
								if (lVersicherter.getAusgewaehlt() || !pNurZugeordneteAnzeigen) {
									lErmittelteArbeitnehmer.add(lVersicherter);
								}
							}
						}

						// Zugeordnete Arbeitnehmer, die nicht mehr ermittelt wurden!
						for (ArbeitnehmerZeitModel lZugeordneterArbeitnehmer : lZugeordneteArbeitnehmer) {
							VersicherungsZeitModel lVersicherungsZeit = VersicherungsZeitModel.loadVersicherungsZeitModel(lZugeordneterArbeitnehmer.getModelOid(), pContext);
							VersicherteViewModel lVersicherter = createVersicherteView(pContext, lVersicherungsZeit, pZuordnung, pReferenzZeitraum);
							fillVersicherteView(lVersicherter, lZugeordneterArbeitnehmer);
							lErmittelteArbeitnehmer.add(lVersicherter);
						}
					} catch (Exception ex) {
						pContext.postEvent(SeverityType.ERROR, ex, "Fehler bei der Ermittlung der Arbeitnehmer: " + ex.getMessage());

					} finally {
						// Cursor schließen
						if (lVersicherungsZeitenZuBetriebsnummer != null) {
							try {
								lVersicherungsZeitenZuBetriebsnummer.close();
							} catch (Exception ex) {
								// Nicht schön aber ersteinmal egal...
							}
						}
					}
				}

			}
		}
		return Collections.unmodifiableCollection(lErmittelteArbeitnehmer);
	}

	private static void fillVersicherteView(VersicherteViewModel pVersicherter, ArbeitnehmerZeitModel pZugeordneterArbeitnehmer) {
		pVersicherter.setAusgewaehlt(true);
		pVersicherter.setBeginnDatum(pZugeordneterArbeitnehmer.getBeginnDatum());
		pVersicherter.setEndeDatum(pZugeordneterArbeitnehmer.getEndeDatum());
		pVersicherter.setArbeitnehmerAnteil(pZugeordneterArbeitnehmer.getArbeitnehmerAnteil());
		pVersicherter.setUnkenntnisInsolvenz(pZugeordneterArbeitnehmer.isUnkenntnisInsolvenzNull() ? false : pZugeordneterArbeitnehmer.getUnkenntnisInsolvenz());
		pVersicherter.setGemeldeteZeit(pZugeordneterArbeitnehmer);
		pVersicherter.setHatLohnabrechnung(CollectionUtils.isNotEmpty(pZugeordneterArbeitnehmer.getLohnabrechnung()));
	}

	private static VersicherteViewModel createVersicherteView(final Context pContext, VersicherungsZeitModel pVersicherungsZeit, int pZuordnung, Intervall pReferenzZeitraum) {
		Assert.isNotNull(pContext, "pContext is null!");
		Assert.isNotNull(pVersicherungsZeit, "pVersicherungsZeit is null!");

		VersicherteViewModel lVersicherter = new VersicherteViewModel(pContext);
		pVersicherungsZeit.setContext(pContext);
		lVersicherter.setVersicherungsZeit(pVersicherungsZeit);
		lVersicherter.setAusgewaehlt(false);

		// Flag Hat Kinder bei Aufruf aus Insolvenzgeld setzen
		if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD) {
			lVersicherter.setHatKinder(isHatKinder(pVersicherungsZeit.getVersichertePerson(), pReferenzZeitraum));
		}
		return lVersicherter;
	}

	public static Intervall ermittleReferenzZeitraum(Context pContext, InsolvenzModel pInsolvenz, InsolvenzgeldAntragModel pInsolvenzgeldAntrag, int pZuordnung, Date pZeitraumVon, Date pZeitraumBis) {
		Assert.isNotNull(pInsolvenz, "pInsolvenz is null!");

		Date lZeitenVon;
		Date lZeitenBis;
		Date lReferenzDatum = null;
		Date lMinDatumRueckstand = null;
		if (pZeitraumBis == null) {
			// PCR 128487: Änderung der Ermittlung des Referenzdatums
			lReferenzDatum = ServiceFactory.getInstance(InsolvenzService.class).getDatumForderungsermittlung(pInsolvenz);
			lZeitenVon = DateUtil.newMinValue();
			lZeitenBis = DateUtil.newMaxValue();
		} else {
			lZeitenVon = pZeitraumVon;
			lZeitenBis = pZeitraumBis;
		}

		if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZ) {
			if (pZeitraumBis == null) {
				lZeitenVon = DateUtil.subtractYear(lReferenzDatum, 10);
				lZeitenVon = DateUtil.newDate(DateUtil.getYear(lZeitenVon), 1, 1);
				lZeitenBis = lReferenzDatum;
			}
		} else if (pZuordnung == InsolvenzService.ZUORDNUNG_BETRIEBSPRUEFUNG) {
			if (pZeitraumBis == null) {
				lZeitenVon = DateUtil.subtractYear(lReferenzDatum, 4);
				lZeitenVon = DateUtil.newDate(DateUtil.getYear(lZeitenVon), 1, 1);
				lZeitenBis = lReferenzDatum;
			}
		} else if (pZuordnung == InsolvenzService.ZUORDNUNG_INSOLVENZGELD) {
			lMinDatumRueckstand = ermittleRueckstandszeitraum(pContext, pInsolvenzgeldAntrag).getVon();
			if (pZeitraumBis == null) {
				lZeitenVon = DateUtil.subtractMonth(lReferenzDatum, 3);
				Calendar cal = Calendar.getInstance();
				cal.setTime(lReferenzDatum);

				if ((cal.get(Calendar.MONTH) == Calendar.MAY)
						&& (cal.get(Calendar.DAY_OF_MONTH) == 30 || cal.get(Calendar.DAY_OF_MONTH) == 31 || (cal.get(Calendar.DAY_OF_MONTH) == 29 && new GregorianCalendar().isLeapYear(cal
								.get(Calendar.YEAR))))) {
					cal.set(Calendar.MONTH, Calendar.MARCH);
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
					lZeitenVon = cal.getTime();
				}

				// Alle Arbeitnehmer ermitteln, die von einem offenen Beitrag "betroffen" sind
				if (lMinDatumRueckstand != null && lZeitenVon.after(lMinDatumRueckstand)) {
					lZeitenVon = lMinDatumRueckstand;
				}
				lZeitenBis = lReferenzDatum;
			}
		}
		return new Intervall(lZeitenVon, lZeitenBis);
	}

	public static boolean isBetragForderungenManuell(Context pContext, InsolvenzgeldAntragModel pInsolvenzgeldAntrag, int pZuordnung) {
		Assert.isNotNull(pContext, "pContext is null!");
		Assert.isNotNull(pInsolvenzgeldAntrag, "pInsolvenzgeldAntrag is null!");

		Intervall lReferenzZeitraum = ermittleReferenzZeitraum(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz(), pInsolvenzgeldAntrag, pZuordnung, null, null);

		Collection<VersicherteViewModel> lErmittelteArbeitnehmer = ermittleArbeitnehmer(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz(), true, pZuordnung, lReferenzZeitraum);

		/*
		 * Wenn 'Rückstände von' ungleich dem kleinsten 'Rückstandszeitraum von' aus der Arbeitnehmeraufstellung oder 'Rückstände bis' ungleich dem größten 'Rückstandszeitraum bis' aus der
		 * Arbeitnehmeraufstellung oder der Betrag der automatischen Ermittlung mit Rückstandszeitraum ungleich dem Betrag Forderungen
		 */
		return isRueckstaendeVonNotEqualToLatestRueckstandszeitraumVon(pInsolvenzgeldAntrag, lErmittelteArbeitnehmer)
				|| isRueckstaendeBisNotEqualToRecentRueckstandszeitraumBis(pInsolvenzgeldAntrag, lErmittelteArbeitnehmer)
				|| isBetragErmitteltNotEqualToBetragForderungen(pContext, pInsolvenzgeldAntrag);
	}

	private static boolean isBetragErmitteltNotEqualToBetragForderungen(Context pContext, InsolvenzgeldAntragModel pInsolvenzgeldAntrag) {
		boolean lIsBetragForderungenManuell = false;

		BigDecimal llErmittelterBetrag = ZahlungsverkehrConstant.ZEROBIGDECIMAL;
		BigDecimal lRueckstaendigeBeitraegeForderungen = pInsolvenzgeldAntrag.getRueckstaendigeBeitraegeForderungen();

		Collection<RueckstandViewModel> lErmittelteRueckstaende = InsolvenzPaUtil.ermittleRueckstaende(pContext, pInsolvenzgeldAntrag.getInsolvenzgeld().getInsolvenz(),
				pInsolvenzgeldAntrag.getRueckstaendigVon(), pInsolvenzgeldAntrag.getRueckstaendigBis(), pInsolvenzgeldAntrag);
		for (RueckstandViewModel lRueckstand : lErmittelteRueckstaende) {
			if (lRueckstand.getKontobewegung() != null) {
				llErmittelterBetrag = DecimalUtil.add(llErmittelterBetrag, lRueckstand.getBetragErmittelt());
			}
		}
		if (lRueckstaendigeBeitraegeForderungen != null && llErmittelterBetrag.compareTo(ZahlungsverkehrConstant.ZEROBIGDECIMAL) > 0) {
			lIsBetragForderungenManuell = llErmittelterBetrag.compareTo(lRueckstaendigeBeitraegeForderungen) != 0;
		}
		return lIsBetragForderungenManuell;
	}

	private static boolean isRueckstaendeBisNotEqualToRecentRueckstandszeitraumBis(InsolvenzgeldAntragModel pInsolvenzgeldAntrag, Collection<VersicherteViewModel> pErmittelteArbeitnehmer) {
		boolean lIsBetragForderungenManuell = false;

		Date lRueckstaendigBis = pInsolvenzgeldAntrag.getRueckstaendigBis();
		NavigableSet<Date> lRueckstandszeitraeumeBis = new TreeSet<>();

		for (VersicherteViewModel lVersicherte : pErmittelteArbeitnehmer) {
			final Date lEndeDatum = lVersicherte.getEndeDatum() == null ? ZahlungsverkehrConstant.MAX_DATE : lVersicherte.getEndeDatum();
			lRueckstandszeitraeumeBis.add(lEndeDatum);
		}

		if (lRueckstaendigBis != null && CollectionUtils.isNotEmpty(lRueckstandszeitraeumeBis)) {
			lIsBetragForderungenManuell = lRueckstaendigBis.compareTo(lRueckstandszeitraeumeBis.last()) != 0;
		}
		return lIsBetragForderungenManuell;
	}

	private static boolean isRueckstaendeVonNotEqualToLatestRueckstandszeitraumVon(InsolvenzgeldAntragModel pInsolvenzgeldAntrag, Collection<VersicherteViewModel> pErmittelteArbeitnehmer) {
		boolean lIsBetragForderungenManuell = false;

		Date lRueckstaendigVon = pInsolvenzgeldAntrag.getRueckstaendigVon();
		NavigableSet<Date> lRueckstandszeitraeumeVon = new TreeSet<>();

		for (VersicherteViewModel lVersicherte : pErmittelteArbeitnehmer) {
			final Date lBeginnDatum = lVersicherte.getBeginnDatum() == null ? ZahlungsverkehrConstant.MIN_DATE : lVersicherte.getBeginnDatum();
			lRueckstandszeitraeumeVon.add(lBeginnDatum);
		}

		if (lRueckstaendigVon != null && CollectionUtils.isNotEmpty(lRueckstandszeitraeumeVon)) {
			lIsBetragForderungenManuell = lRueckstaendigVon.compareTo(lRueckstandszeitraeumeVon.first()) != 0;
		}
		return lIsBetragForderungenManuell;
	}

	public static InsolvenzBetraege ermittleSummeOffeneForderungen(Context pContext, InsolvenzModel pInsolvenz, int pZuordnung) {
		InsolvenzService lService = ServiceFactory.getInstance(InsolvenzService.class);
		InsolvenzBetraege lResult = lService.getNewInsolvenzBetraege();
		Date lReferenzdatum = lService.getDatumForderungsermittlung(pInsolvenz);
		Map<Class<? extends GrundlagenModel>, GrundlagenModel> lGrundlagen = getGrundlagen(pContext, lReferenzdatum, lReferenzdatum);
		GesetzlicheGrundlagenModel lGesetzlicheGrundlage = retrieve(GesetzlicheGrundlagenModel.class, lGrundlagen);
		GrundlagenSatzungKVModel lGrundlageKV = retrieve(GrundlagenSatzungKVModel.class, lGrundlagen);

		for (ForderungInsolvenzViewModel lForderung : ServiceFactory.getInstance(InsolvenzService.class).ermittleForderungen(pContext, pInsolvenz, false, pZuordnung)) {
			lResult.mBetrag = lResult.mBetrag.add(lForderung.getBetragForderung());
			lResult.mOffenerBetrag = lResult.mOffenerBetrag.add(lForderung.getBetragForderungOffen());
			BigDecimal lAnAnteil = ermittleANAnteilZurForderung(lForderung.getFordVerb(), lGesetzlicheGrundlage, lGrundlageKV);
			lForderung.setBetragAnAnteil(lAnAnteil);
			lResult.mOffenerANBetrag = lResult.mOffenerANBetrag.add(lAnAnteil);
		}
		return lResult;
	}

	private static BigDecimal ermittleANAnteilZurForderung(ForderungVerbindlichkeitModel lForderung, GesetzlicheGrundlagenModel pGesetzlicheGrundlage, GrundlagenSatzungKVModel pGrundlageKV) {
		BigDecimal lResult = ZahlungsverkehrConstant.ZEROBIGDECIMAL;

		for (KontoBewegungPositionModel lPosition : lForderung.getKontobewegung().getKontoBewegungPosition()) {
			if (BELEGPOSITIONSARTEN_KV_BEITRAG.contains(lPosition.getBelegPositionsArt())) {
				BigDecimal lBetrag = getKvAnAnteil(lForderung.getKontobewegung(), lPosition.getOffenerBetrag(), pGrundlageKV);
				lResult = lResult.add(lBetrag == null ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : lBetrag);
			} else if (BELEGPOSITIONSARTEN_RV_BEITRAG.contains(lPosition.getBelegPositionsArt())) {
				BigDecimal lBetrag = getRvAnAnteil(lPosition.getOffenerBetrag());
				lResult = lResult.add(lBetrag == null ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : lBetrag);
			} else if (BELEGPOSITIONSARTEN_AV_BEITRAG.contains(lPosition.getBelegPositionsArt())) {
				BigDecimal lBetrag = getAvAnAnteil(lPosition.getOffenerBetrag());
				lResult = lResult.add(lBetrag == null ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : lBetrag);
			} else if (BELEGPOSITIONSARTEN_PV_BEITRAG.contains(lPosition.getBelegPositionsArt())) {
				BigDecimal lBetrag = getPvAnAnteil(lPosition.getOffenerBetrag(), null, null, pGesetzlicheGrundlage);
				lResult = lResult.add(lBetrag == null ? ZahlungsverkehrConstant.ZEROBIGDECIMAL : lBetrag);
			} else if (BELEGPOSITIONSARTEN_PFLZB_ZUSATZBEITRAG.contains(lPosition.getBelegPositionsArt())) {
				BigDecimal lBetrag = lPosition.getOffenerBetrag();
				if (isValidValue(lBetrag)) {
					lResult = lResult.add(lBetrag);
				}
			}
		}
		return lResult;
	}
}
