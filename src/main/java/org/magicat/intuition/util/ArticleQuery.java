package org.mskcc.knowledge.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mskcc.knowledge.model.Article;
import org.mskcc.knowledge.model.MutationMap;
import org.mskcc.knowledge.model.Target;
import org.mskcc.knowledge.model.TumorTypes;
import org.mskcc.knowledge.repository.MutationMapRepository;
import org.mskcc.knowledge.repository.TargetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mskcc.knowledge.util.SolrClientTool.escape;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class ArticleQuery {

    public final static Logger log = LoggerFactory.getLogger(ArticleQuery.class);


    TargetRepository targetRepository;
    MutationMapRepository mutationMapRepository;

    private List<String> cancers = readCancerNames();
    private List<String> drugs = readDrugs();
    private List<String> genes = Arrays.asList("ABL1,AKT1,ALK,AMER1,APC,AR,ARID1A,ASXL1,ATM,ATRX,AXIN1,BAP1,BCL2,BCOR,BRAF,BRCA1,BRCA2,CARD11,CBL,CDC73,CDH1,CDKN2A,CEBPA,CIC,CREBBP,CTNNB1,DAXX,DNMT3A,EGFR,EP300,ERBB2,EZH2,FBXW7,FGFR2,FGFR3,FLT3,FOXL2,GATA3,GNA11,GNAQ,GNAS,HNF1A,HRAS,IDH1,IDH2,JAK1,JAK2,JAK3,KDM5C,KDM6A,KIT,KMT2D,KRAS,MAP2K1,MAP3K1,MED12,MEN1,MET,MLH1,MPL,MSH2,MSH6,MYD88,NF1,NF2,NFE2L2,NOTCH1,NOTCH2,NPM1,NRAS,PAX5,PBRM1,PDGFRA,PIK3CA,PIK3R1,PPP2R1A,PRDM1,PTCH1,PTEN,PTPN11,RB1,RET,RNF43,SETD2,SF3B1,SMAD2,SMAD4,SMARCA4,SMARCB1,SMO,SOCS1,SPOP,STAG2,STK11,TET2,TNFAIP3,TP53,TSC1,U2AF1,VHL,WT1,AKT2,ARID2,ATR,B2M,BARD1,BCL6,BRD4,BRIP1,BTK,CALR,CASP8,CBFB,CCND1,CCND2,CCND3,CCNE1,CD274,CD79A,CD79B,CDK12,CDK4,CDK6,CDKN1B,CDKN2C,CHEK2,CRLF2,CSF1R,CSF3R,CTCF,CXCR4,DDR2,ERBB3,ERBB4,ERG,ESR1,ETV6,FANCA,FANCC,FGFR1,FGFR4,FLCN,FUBP1,GATA1,GATA2,H3-3A,H3C2,IKZF1,IRF4,JUN,KDM5A,KDR,KEAP1,KMT2A,KMT2C,MAP2K2,MAP2K4,MAPK1,MDM2,MDM4,MITF,MTOR,MUTYH,MYC,MYCL,MYCN,NKX2-1,NSD2,NSD3,NTRK1,NTRK3,PALB2,PDCD1LG2,PDGFRB,PHF6,PIM1,PRKAR1A,RAD21,RAF1,RARA,ROS1,RUNX1,SDHA,SDHB,SDHC,SDHD,SOX2,SPEN,SRC,SRSF2,STAT3,SUFU,SYK,TENT5C,TGFBR2,TMPRSS2,TNFRSF14,TSC2,TSHR,XPO1,AKT3,ARAF,ARID1B,AURKA,AURKB,AXL,BCL10,BCORL1,BCR,BIRC3,BLM,BTG1,CDK8,CDKN2B,CHEK1,CRKL,CYLD,DOT1L,EED,EIF4A2,EPHA3,EPHB1,ERCC4,ETV1,FAS,FGF19,FGF3,FGF4,FH,FLT1,FLT4,FOXO1,FOXP1,GRIN2A,GSK3B,HGF,ID3,IGF1R,IKBKE,IL7R,INPP4B,IRS2,KLF4,LMO1,MALT1,MAP3K13,MCL1,MEF2B,MRE11,MSH3,MSI2,NBN,NCOR1,NFKBIA,NSD1,NT5C2,NTRK2,P2RY8,PDCD1,PIK3CB,PMS2,POLD1,POLE,POT1,PPARG,RAC1,RAD51,RAD51B,RBM10,REL,RHOA,RICTOR,RPTOR,SETBP1,SOX9,STAT5B,SUZ12,TBX3,TCF3,TERT,TET1,TOP1,TP63,TRAF7,ZRSR2,ACVR1,ALOX12B,AXIN2,BCL11B,BCL2L1,BMPR1A,CDKN1A,CIITA,CUL3,CUX1,DDX3X,DICER1,DIS3,DNAJB1,DNMT1,DROSHA,EPAS1,EPHA5,EPHA7,ERCC2,ERCC3,ERCC5,ERRFI1,ETV4,ETV5,EWSR1,FANCD2,FAT1,FBXO11,FOXA1,GLI1,GNA13,H1-2,H3-3B,HDAC1,HLA-A,INHBA,LATS1,LATS2,LYN,MAF,MAP3K14,MAX,MLLT1,MST1R,MYOD1,NCOR2,NOTCH3,NUP93,PARP1,PHOX2B,PIK3C2G,PIK3CG,PIK3R2,PLCG2,PPM1D,PPP6C,PREX2,PRKCI,PRKN,PTPRT,RAD50,RAD51C,RAD51D,RAD52,RAD54L,RECQL4,RUNX1T1,SDHAF2,SGK1,SH2B3,SMAD3,SMARCD1,STAT5A,STAT6,TBL1XR1,TCF7L2,TEK,TMEM127,TRAF2,VEGFA,WWTR1,XRCC2,ZFHX3,ABL2,ABRAXAS1,AGO2,ANKRD11,ARID5B,ASXL2,ATF1,BABAM1,BBC3,BCL2L11,BCL9,CARM1,CCNQ,CD276,CD58,CDC42,CENPA,COP1,CSDE1,CTLA4,CYSLTR2,DCUN1D1,DDIT3,DEK,DNMT3B,DTX1,DUSP22,DUSP4,E2F3,EGFL7,EIF1AX,EIF4E,ELF3,ELOC,EPCAM,ERF,ETNK1,EZH1,FANCG,FANCL,FEV,FLI1,FYN,GNA12,GNB1,GPS2,GREM1,H1-3,H1-4,H2AC11,H2AC16,H2AC17,H2AC6,H2BC11,H2BC12,H2BC17,H2BC4,H2BC5,H3-4,H3-5,H3C1,H3C10,H3C11,H3C12,H3C13,H3C14,H3C3,H3C4,H3C6,H3C7,H3C8,HDAC4,HDAC7,HIF1A,HLA-B,HOXB13,ICOSLG,IFNGR1,IGF1,IGF2,IKZF3,IL10,INHA,INPP4A,INPPL1,INSR,IRF1,IRF8,IRS1,JARID2,KAT6A,KMT2B,KMT5A,KNSTRN,LCK,LMO2,LZTR1,MAFB,MAPK3,MAPKAP1,MDC1,MECOM,MGA,MLLT10,MSI1,MST1,MTAP,MYB,NCOA3,NCSTN,NEGR1,NKX3-1,NOTCH4,NR4A3,NTHL1,NUF2,NUP98,NUTM1,PAK1,PAK5,PCBP1,PDGFB,PDPK1,PGR,PIK3C3,PIK3CD,PIK3R3,PLCG1,PLK2,PMAIP1,PMS1,PNRC1,PPP4R2,PRDM14,PRKD1,PTP4A1,PTPN2,PTPRD,PTPRS,RAB35,RAC2,RASA1,RBM15,RECQL,RHEB,RIT1,RPS6KA4,RPS6KB2,RRAGC,RRAS,RRAS2,RTEL1,RXRA,RYBP,SESN1,SESN2,SESN3,SETDB1,SH2D1A,SHOC2,SHQ1,SLX4,SMARCE1,SMC1A,SMC3,SMYD3,SOS1,SOX17,SPRED1,SS18,STK19,STK40,TAL1,TAP1,TAP2,TCL1A,TFE3,TGFBR1,TLX1,TLX3,TP53BP1,TRAF3,TRAF5,TYK2,U2AF2,UBR5,UPF1,USP8,VTCN1,XBP1,XIAP,YAP1,YES1,ABI1,ACTG1,ACVR1B,AFDN,AFF1,AFF4,AGO1,ALB,APLNR,ARFRP1,ARHGAP26,ARHGAP35,ARHGEF12,ARHGEF28,ARID3A,ARID3B,ARID3C,ARID4A,ARID4B,ARID5A,ARNT,ATIC,ATP6AP1,ATP6V1B2,ATXN2,ATXN7,BACH2,BCL11A,BCL2L2,BCL3,BCL7A,BTG2,CAMTA1,CARS1,CBFA2T3,CD22,CD28,CD70,CD74,CDX2,CEP43,CLTC,CLTCL1,CMTR2,CNTRL,COL1A1,CRBN,CREB1,CREB3L1,CREB3L2,CTNNA1,CTR9,CYP19A1,DDX10,DDX6,DNM2,EBF1,ECT2L,EGR1,ELF4,ELL,EML4,EMSY,EP400,EPOR,EPS15,ESCO2,ETAA1,EZHIP,EZR,FANCE,FANCF,FCGR2B,FCRL4,FGF10,FGF14,FGF23,FGF6,FHIT,FOXF1,FOXO3,FOXO4,FSTL3,FURIN,FUS,GAB1,GAB2,GAS7,GID4,GPHN,GTF2I,H1-5,H2BC8,H4C9,HERPUD1,HEY1,HIP1,HLA-C,HLF,HMGA1,HMGA2,HOXA11,HOXA13,HOXA9,HOXC11,HOXC13,HOXD11,HOXD13,HSP90AA1,HSP90AB1,IGH,IGK,IGL,IL21R,IL3,ITK,KBTBD4,KDSR,KIF5B,KLF5,KLHL6,KSR2,LASP1,LEF1,LPP,LRP1B,LTB,LYL1,MAD2L2,MGAM,MLF1,MLLT3,MLLT6,MN1,MOB3B,MPEG1,MRTFA,MSN,MUC1,MYH11,MYH9,NADK,NCOA2,NDRG1,NFE2,NFKB2,NIN,NRG1,NUMA1,NUP214,NUTM2A,PAFAH1B2,PAX3,PAX7,PAX8,PBX1,PCM1,PDE4DIP,PDK1,PDS5B,PER1,PGBD5,PICALM,PIGA,PLAG1,PML,POU2AF1,PPP2R2A,PRDM16,PRKACA,PRRX1,PSIP1,PTPN1,PTPRO,QKI,RABEP1,RAP1GDS1,RELN,REST,RHOH,RNF213,ROBO1,RPL22,RPN1,RSPO2,SAMHD1,SCG5,SDC4,SERPINB3,SERPINB4,SET,SETD1A,SETD1B,SETD3,SETD4,SETD5,SETD6,SETD7,SETDB2,SH3GL1,SLC34A2,SLFN11,SMARCA2,SMG1,SOCS3,SP140,SPRTN,SRSF3,SSX1,SSX2,SSX4,STAG1,TAF15,TAL2,TET3,TFG,TNFRSF17,TPM3,TPM4,TRA,TRB,TRD,TRG,TRIM24,TRIP11,TRIP13,USP6,VAV1,VAV2,WIF1,ZBTB16,ZMYM2,ZNF217,ZNF384,ZNF521,ZNF703,ZNRF3,ACKR3,ACSL3,ACSL6,ACTB,ACVR2A,ADGRA2,AFF3,AJUBA,APH1A,APOBEC3B,ASMTL,ASPSCR1,ATG5,ATP1A1,ATP2B3,BAX,BCL9L,BRD3,BRSK1,BTLA,BUB1B,CACNA1D,CAD,CANT1,CBLB,CBLC,CCDC6,CCN6,CCNB1IP1,CCNB3,CCT6B,CD36,CDH11,CHCHD7,CHD2,CHD4,CHIC2,CHN1,CILK1,CKS1B,CLIP1,CLP1,CNBP,CNOT3,COL2A1,CPS1,CRTC1,CRTC3,CSF1,CUL4A,CYP17A1,DAZAP1,DCTN1,DDB2,DDR1,DDX4,DDX41,DDX5,DKK1,DKK2,DKK3,DKK4,DUSP2,DUSP9,EIF3E,ELK4,ELN,ELP2,EPHB4,ERC1,ETS1,EXOSC6,EXT1,EXT2,FAF1,FAT4,FBXO31,FES,FGF12,FIP1L1,FLYWCH1,FNBP1,FRS2,GABRA6,GADD45B,GATA4,GATA6,GMPS,GOLGA5,GOPC,GPC3,GRM3,GTSE1,H3C15,H3P6,HIRA,HNRNPA2B1,HOOK3,HOXA3,HSD3B1,IKBKB,IKZF2,IL2,IL6ST,INPP5D,IRF2,IRS4,JAZF1,KAT6B,KCNJ5,KDM2B,KDM4C,KEL,KLF2,KLF3,KLF6,KLK2,KNL1,KTN1,LARP4B,LCP1,LIFR,LMNA,LRIG3,LRP5,LRP6,LRRK2,LTK,MAGED1,MAML2,MAP3K6,MAP3K7,MBD6,MDS2,MEF2C,MEF2D,MERTK,MIB1,MIDEAS,MKI67,MKNK1,MLLT11,MNX1,MTCP1,MYO18A,MYO5A,NAB2,NACA,NBEAP1,NCOA1,NCOA4,NFATC2,NFIB,NFKBIE,NOD1,NONO,NUTM2B,OLIG2,OMD,PAG1,PAK3,PARP2,PARP3,PASK,PATZ1,PC,PCLO,PCSK7,PDCD11,PHF1,PIK3C2B,POLQ,POU5F1,PPFIBP1,PPP1CB,PRCC,PRF1,PRKDC,PRSS1,PRSS8,PTK6,PTK7,PTPN13,PTPN6,PTPRB,PTPRC,PTPRK,RALGDS,RANBP2,RASGEF1A,RMI2,RNF217-AS1,RPL10,RPL5,RSPO3,RUNX2,S1PR2,SALL4,SBDS,SEC31A,SEPTIN5,SEPTIN6,SEPTIN9,SERP2,SFPQ,SFRP1,SFRP2,SFRP4,SIX1,SLC1A2,SLC45A3,SMARCA1,SNCAIP,SND1,SNX29,SOCS2,SOX10,SS18L1,STAT1,STAT2,STAT4,STIL,STRN,TAF1,TCEA1,TCF12,TCL1B,TEC,TERC,TFEB,TFPT,TFRC,TIPARP,TLE1,TLE2,TLE3,TLE4,TLL2,TMEM30A,TMSB4XP8,TNFRSF11A,TPR,TRIM27,TRIM33,TRRAP,TTL,TUSC3,TYRO3,WDCP,WDR90,WRN,XPA,XPC,YPEL5,YWHAE,YY1,YY1AP1,ZBTB20,ZBTB7A,ZFP36L1,ZMYM3,ZNF24,ZNF331,ZNF750".split(","));

    private Map<String, Pattern> genePatternMap = new HashMap<>();
    private Map<String, Pattern> drugPatternMap = new HashMap<>();
    private Map<String, Pattern> cancerPatternMap = new HashMap<>();
    private Map<String, Pattern> mutationPatternMap = new HashMap<>();

    private Map<String, String> geneSynonymMap = new HashMap<>();

    private Map<String, List<WordPair>> mutations;


    public List<String> getCancers() {
        return cancers;
    }

    public void setCancers(List<String> cancers) {
        this.cancers = cancers;
    }

    public List<String> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<String> drugs) {
        this.drugs = drugs;
    }

    public Map<String, List<WordPair>> getMutations() {
        return mutations;
    }

    public void setMutations(Map<String, List<WordPair>> mutations) {
        this.mutations = mutations;
    }

    public List<String> getGenes() {
        return genes;
    }

    public void setGenes(List<String> genes) {
        this.genes = genes;
    }

    public Map<String, Pattern> getGenePatternMap() {
        return genePatternMap;
    }

    public void setGenePatternMap(Map<String, Pattern> genePatternMap) {
        this.genePatternMap = genePatternMap;
    }

    public Map<String, Pattern> getDrugPatternMap() {
        return drugPatternMap;
    }

    public void setDrugPatternMap(Map<String, Pattern> drugPatternMap) {
        this.drugPatternMap = drugPatternMap;
    }

    public Map<String, Pattern> getCancerPatternMap() {
        return cancerPatternMap;
    }

    public void setCancerPatternMap(Map<String, Pattern> cancerPatternMap) {
        this.cancerPatternMap = cancerPatternMap;
    }

    public Map<String, Pattern> getMutationPatternMap() {
        return mutationPatternMap;
    }

    public void setMutationPatternMap(Map<String, Pattern> mutationPatternMap) {
        this.mutationPatternMap = mutationPatternMap;
    }

    public Map<String, String> getGeneSynonymMap() {
        return geneSynonymMap;
    }

    public void setGeneSynonymMap(Map<String, String> geneSynonymMap) {
        this.geneSynonymMap = geneSynonymMap;
    }

    public void setTargetRepository(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public void setMutationMapRepository(MutationMapRepository mutationMapRepository) {
        this.mutationMapRepository = mutationMapRepository;
    }

    public void buildGenePatterns(boolean geneCleanup) {
        for (String gene : genes) {
            String regex = "(([\\W\\s^_]|^)(" + gene.toLowerCase() + ")([\\W\\s^_]|$))";
            Iterator<Target> it = targetRepository.findAllBySymbol(gene).iterator();
            if (!it.hasNext()) {
                //System.out.println("NO GENE " + gene + " FOUND, USING SIMPLE NAME");
                genePatternMap.put(gene.toLowerCase(), Pattern.compile(regex));
            } else if (!geneCleanup) {

                Target t = it.next();

                if (t.getSynonyms() != null && t.getSynonyms().length() > 0) {
                    String[] synonyms = t.getSynonyms().split(";");
                    for (int i = 0; i < Math.min(5, synonyms.length); i++)
                        if (!synonyms[i].equalsIgnoreCase("at") && !synonyms[i].equalsIgnoreCase("men") && !synonyms[i].equalsIgnoreCase("the") && !synonyms[i].equalsIgnoreCase("as") && !synonyms[i].equalsIgnoreCase("ii"))
                            regex += "|" + "(([\\W\\s^_]|^)(" + synonyms[i].toLowerCase() + ")([\\W\\s^_]|$))";
                }
                genePatternMap.put(gene.toLowerCase(), Pattern.compile(regex));
                geneSynonymMap.put(gene.toLowerCase(), t.getSynonyms());

            }
        }
        for (String gene: genePatternMap.keySet()) log.info(gene + " " + genePatternMap.get(gene).pattern());

        log.info(genes.size() + " genes and " + genePatternMap.size() + " patterns");

    }

    public void buildDrugPatterns() {
        for (String drug : drugs) {
            String regex = "([\\W\\s^_]|^)(" + drug.toLowerCase() + ")([\\W\\s^_]|$)";
            drugPatternMap.put(drug.toLowerCase(), Pattern.compile(regex));
        }
        for (String drug: drugPatternMap.keySet()) System.out.println(drug + " " + drugPatternMap.get(drug).pattern());
        log.info(drugs.size() + " drugs and " + drugPatternMap.size() + " patterns");
    }

    public void buildCancerPatterns() {
        for (String cancer : cancers) {
            String regex = "([\\W\\s^_]|^)(" + cancer.toLowerCase() + ")([\\W\\s^_]|$)";
            cancerPatternMap.put(cancer.toLowerCase(), Pattern.compile(regex));
        }
        for (String cancer: cancerPatternMap.keySet()) System.out.println(cancer + " " + cancerPatternMap.get(cancer).pattern());

        log.info(cancers.size() + " cancer types and " + cancerPatternMap.size() + " patterns");

    }

    public void buildMutationPatterns() {
        AminoAcids.populate();
        Map<String, List<WordPair>> mutationMap = readVariants();
        HashSet<WordPair> set = new HashSet<>();
        for (String mutation: mutationMap.keySet()) {
            for (WordPair wp : mutationMap.get(mutation)) {
                int size = set.size();
                set.add(wp);

                if (set.size() > size) {
                    String regex = "(([\\W\\s^_]|^)(" + escape(wp.word1.toLowerCase()) + ")([\\W\\s^_]|$))";
                    if (wp.word2 != null && !wp.word2.equals("") && !wp.word2.equalsIgnoreCase(wp.word1)) regex += "|" + "(([\\W\\s^_]|^)(" + escape(wp.word2.toLowerCase()) + ")([\\W\\s^_]|$))";
                    mutationPatternMap.put(wp.word1.toLowerCase(), Pattern.compile(regex));
                }
            }
        }
        List<MutationMap> extras = mutationMapRepository.findAllWithSynonyms();
        for (MutationMap mm : extras) {
            String regex = "(([\\W\\s^_]|^)(" + escape(mm.getSymbol().toLowerCase()) + ")([\\W\\s^_]|$))";
            if (mm.getSynonyms().length() > 0) regex += "|" + "(([\\W\\s^_]|^)(" + escape(mm.getSynonyms().toLowerCase()) + ")([\\W\\s^_]|$))";
            mutationPatternMap.put(mm.getSymbol().toLowerCase(), Pattern.compile(regex));
        }

        for (String mutation: mutationPatternMap.keySet()) System.out.println(mutation + " " + mutationPatternMap.get(mutation).pattern());
        log.info(set.size() + " mutations and " + mutationPatternMap.size() + " patterns");
    }

    public void buildMutationPatternsOther() {
        AminoAcids.populate();
        Map<String, List<WordPair>> mutationMap = readVariants();
        HashSet<WordPair> set = new HashSet<>();
        for (String gene: mutationMap.keySet()) {
            for (WordPair wp : mutationMap.get(gene)) {
                int size = set.size();
                set.add(wp);

                if (set.size() > size) {
                    String regex = "(([\\W\\s^_]|^)(" + wp.word1.toLowerCase() + ")([\\W\\s^_]|$))";
                    if (!wp.word2.equals("")) regex += "|" + "(([\\W\\s^_]|^)(" + wp.word2.toLowerCase() + ")([\\W\\s^_]|$))";
                    mutationPatternMap.put(wp.word1.toLowerCase(), Pattern.compile(regex));
                }
            }
        }
    }

    public static List<String> readCancerNames() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < TumorTypes.basicTypes.length; i++) {
            String item = TumorTypes.basicTypes[i];
            String searchTerm = "";
            String searchTerm2 = null;
            if (item.endsWith(", NOS")) item = item.substring(0, item.length() - 5);
            String[] words = item.split(" ");
            boolean nextWord = false;
            for (String word : words) {
                if (word.contains("/")) {
                    String[] w = word.split("/");
                    searchTerm2 = searchTerm;
                    searchTerm += (searchTerm.length() == 0 ? w[0] : " " + w[0]);
                    searchTerm2 += (searchTerm2.length() == 0 ? w[1] : " " + w[1]);
                    if (w[1].equals("Fallopian") || w[1].equals("Urinary")) nextWord = true;
                } else {
                    if (!nextWord) {
                        searchTerm += (searchTerm.length() == 0 ? word : " " + word);
                        if (searchTerm2 != null) searchTerm2 += (searchTerm2.length() == 0 ? word : " " + word);
                    } else {
                        searchTerm2 += (searchTerm2.length() == 0 ? word : " " + word);
                        nextWord = false;
                    }
                }
            }
            result.add(searchTerm);
            if (searchTerm2 != null) result.add(searchTerm);
        }
        for (int i = 0; i < TumorTypes.subTypes.length; i++) {
            String item = TumorTypes.subTypes[i];
            String searchTerm = "";
            String searchTerm2 = null;
            if (item.endsWith(", NOS")) item = item.substring(0, item.length() - 5);
            String[] words = item.split(" ");
            boolean nextWord = false;
            for (String word : words) {
                if (word.contains("/")) {
                    String[] w = word.split("/");
                    searchTerm2 = searchTerm;
                    searchTerm += (searchTerm.length() == 0 ? w[0] : " " + w[0]);
                    searchTerm2 += (searchTerm2.length() == 0 ? w[1] : " " + w[1]);
                    if (w[1].equals("Fallopian") || w[1].equals("Urinary") || w[1].equals("Signet")) nextWord = true;
                } else {
                    if (!nextWord) {
                        searchTerm += (searchTerm.length() == 0 ? word : " " + word);
                        if (searchTerm2 != null) searchTerm2 += (searchTerm2.length() == 0 ? word : " " + word);
                    } else {
                        searchTerm2 += (searchTerm2.length() == 0 ? word : " " + word);
                        nextWord = false;
                    }
                }
            }
            result.add(searchTerm);
            if (searchTerm2 != null) result.add(searchTerm);
        }
        return result;
    }


    public static Map<String, List<WordPair>> tsvReader(File f) {
        Map<String, List<WordPair>> result = new TreeMap<>();
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(f))) {
            String line = TSVReader.readLine();
            int i = 0;
            while ((line = TSVReader.readLine()) != null) {
                String[] items = line.split("\t");
                List<WordPair> item = result.get(items[5]);
                if (AminoAcids.buildExpression(items[7]) != null) {
                    if (items[7].toLowerCase().endsWith("fusion")) items[7] = items[7].toLowerCase().substring(0, items[7].toLowerCase().indexOf("fusion")-1).trim();
                    if (item == null) item = new ArrayList<>();
                    item.add(new WordPair(items[7], AminoAcids.buildExpression(items[7])));
                    //System.out.println(items[7] + " " + AminoAcids.buildExpression(items[7]));
                    result.put(items[5], item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static List<String> readDrugs() {
        List<String> drugs = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("drugs"));
            while (reader.ready()) {
                drugs.add(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drugs;
    }

    public static Map<String, List<WordPair>> readVariants() {
        AminoAcids.populate();
        File f = new File("allAnnotatedVariants.txt");
        return tsvReader(f);
    }


    public static Map<String, List<WordPair>> tsvReaderSimple(File f) {
        Map<String, List<WordPair>> result = new HashMap<>();
        try (BufferedReader TSVReader = new BufferedReader(new FileReader(f))) {
            String line = TSVReader.readLine();
            int i = 0;
            while ((line = TSVReader.readLine()) != null) {
                String[] items = line.split("\t");
                List<WordPair> item = result.get(items[5]);

                if (item == null) item = new ArrayList<>();
                item.add(new WordPair(items[7], items[9]));
                result.put(items[5], item);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, List<WordPair>> readVariantsAll() {
        File f = new File("allAnnotatedVariants.txt");
        return tsvReaderSimple(f);
    }


//ArticleQuery x = new ArticleQueryBuilder().drug("sotorasib");

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class WordPair {
        public String word1, word2;
    }

    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class WordTriple {
        public String word1, word2, word3;
    }

}
