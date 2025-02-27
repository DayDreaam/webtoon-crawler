package com.example.demo.webtoon.platforms.kakaopage.service

import com.example.demo.webtoon.entity.Webtoon
import com.example.demo.webtoon.enums.Platform
import com.example.demo.webtoon.platforms.kakaopage.dto.GetContentHomeOverviewResponse
import com.example.demo.webtoon.platforms.kakaopage.dto.GetStaticLandingGenreSectionResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate

@Service
class KakaoPageWebtoonService(
    private val restTemplate: RestTemplate
) {
    private val GRAPHQL_URL = "https://bff-page.kakao.com/graphql"
    private val objectMapper = jacksonObjectMapper()

    fun createHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/133.0.0.0 Safari/537.36"
            )
            set("Referer", "https://page.kakao.com")
            set("Origin", "https://page.kakao.com")
            set("Sec-Fetch-Mode", "cors")
            set("Sec-Fetch-Site", "same-site")
        }
    }

    fun fetchGenreSection(page: Int): List<GetStaticLandingGenreSectionResponse> {
        val sectionId = "\$sectionId"
        val param = "\$param"
        val query =
            "\n    query staticLandingGenreSection($sectionId: ID!, $param: StaticLandingGenreParamInput!) {\n  staticLandingGenreSection(sectionId: $sectionId, param: $param) {\n    ...Section\n  }\n}\n    \n    fragment Section on Section {\n  id\n  uid\n  type\n  title\n  ... on RecommendSection {\n    isRecommendArea\n    isRecommendedItems\n  }\n  ... on DependOnLoggedInSection {\n    loggedInTitle\n    loggedInScheme\n  }\n  ... on SchemeSection {\n    scheme\n  }\n  ... on MetaInfoTypeSection {\n    metaInfoType\n  }\n  ... on TabSection {\n    sectionMainTabList {\n      uid\n      title\n      isSelected\n      scheme\n      additionalString\n      subTabList {\n        uid\n        title\n        isSelected\n        groupId\n      }\n    }\n  }\n  ... on ThemeKeywordSection {\n    themeKeywordList {\n      uid\n      title\n      scheme\n    }\n  }\n  ... on StaticLandingDayOfWeekSection {\n    isEnd\n    totalCount\n    param {\n      categoryUid\n      businessModel {\n        name\n        param\n      }\n      subcategory {\n        name\n        param\n      }\n      dayTab {\n        name\n        param\n      }\n      page\n      size\n      screenUid\n    }\n    businessModelList {\n      name\n      param\n    }\n    subcategoryList {\n      name\n      param\n    }\n    dayTabList {\n      name\n      param\n    }\n    promotionBanner {\n      ...PromotionBannerItem\n    }\n  }\n  ... on StaticLandingTodayNewSection {\n    totalCount\n    param {\n      categoryUid\n      subcategory {\n        name\n        param\n      }\n      screenUid\n    }\n    categoryTabList {\n      name\n      param\n    }\n    subcategoryList {\n      name\n      param\n    }\n    promotionBanner {\n      ...PromotionBannerItem\n    }\n    viewType\n  }\n  ... on StaticLandingTodayUpSection {\n    isEnd\n    totalCount\n    param {\n      categoryUid\n      subcategory {\n        name\n        param\n      }\n      page\n    }\n    categoryTabList {\n      name\n      param\n    }\n    subcategoryList {\n      name\n      param\n    }\n  }\n  ... on StaticLandingRankingSection {\n    isEnd\n    rankingTime\n    totalCount\n    param {\n      categoryUid\n      subcategory {\n        name\n        param\n      }\n      rankingType {\n        name\n        param\n      }\n      page\n      screenUid\n    }\n    categoryTabList {\n      name\n      param\n    }\n    subcategoryList {\n      name\n      param\n    }\n    rankingTypeList {\n      name\n      param\n    }\n    displayAd {\n      ...DisplayAd\n    }\n    promotionBanner {\n      ...PromotionBannerItem\n    }\n    withOperationArea\n    viewType\n  }\n  ... on StaticLandingGenreSection {\n    isEnd\n    totalCount\n    param {\n      categoryUid\n      subcategory {\n        name\n        param\n      }\n      sortType {\n        name\n        param\n      }\n      page\n      isComplete\n      screenUid\n    }\n    subcategoryList {\n      name\n      param\n    }\n    sortTypeList {\n      name\n      param\n    }\n    displayAd {\n      ...DisplayAd\n    }\n    promotionBanner {\n      ...PromotionBannerItem\n    }\n  }\n  ... on StaticLandingFreeSeriesSection {\n    isEnd\n    totalCount\n    param {\n      categoryUid\n      tab {\n        name\n        param\n      }\n      page\n      screenUid\n    }\n    tabList {\n      name\n      param\n    }\n    promotionBanner {\n      ...PromotionBannerItem\n    }\n  }\n  ... on StaticLandingEventSection {\n    isEnd\n    totalCount\n    param {\n      categoryUid\n      page\n    }\n    categoryTabList {\n      name\n      param\n    }\n  }\n  ... on StaticLandingOriginalSection {\n    isEnd\n    totalCount\n    originalCount\n    param {\n      categoryUid\n      subcategory {\n        name\n        param\n      }\n      sortType {\n        name\n        param\n      }\n      isComplete\n      page\n      screenUid\n    }\n    subcategoryList {\n      name\n      param\n    }\n    sortTypeList {\n      name\n      param\n    }\n    recommendItemList {\n      ...Item\n    }\n  }\n  ... on HelixThemeSection {\n    subtitle\n    isRecommendArea\n  }\n  groups {\n    ...Group\n  }\n}\n    \n\n    fragment PromotionBannerItem on PromotionBannerItem {\n  title\n  scheme\n  leftImage\n  rightImage\n  eventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment EventLogFragment on EventLog {\n  fromGraphql\n  click {\n    layer1\n    layer2\n    setnum\n    ordnum\n    copy\n    imp_id\n    imp_provider\n  }\n  eventMeta {\n    id\n    name\n    subcategory\n    category\n    series\n    provider\n    series_id\n    type\n  }\n  viewimp_contents {\n    type\n    name\n    id\n    imp_area_ordnum\n    imp_id\n    imp_provider\n    imp_type\n    layer1\n    layer2\n  }\n  customProps {\n    landing_path\n    view_type\n    helix_id\n    helix_yn\n    helix_seed\n    content_cnt\n    event_series_id\n    event_ticket_type\n    play_url\n    banner_uid\n  }\n}\n    \n\n    fragment DisplayAd on DisplayAd {\n  sectionUid\n  bannerUid\n  treviUid\n  momentUid\n}\n    \n\n    fragment Item on Item {\n  id\n  type\n  ...BannerItem\n  ...OnAirItem\n  ...CardViewItem\n  ...CleanViewItem\n  ... on DisplayAdItem {\n    displayAd {\n      ...DisplayAd\n    }\n  }\n  ...PosterViewItem\n  ...StrategyViewItem\n  ...RankingListViewItem\n  ...NormalListViewItem\n  ...MoreItem\n  ...EventBannerItem\n  ...PromotionBannerItem\n  ...LineBannerItem\n}\n    \n\n    fragment BannerItem on BannerItem {\n  bannerType\n  bannerViewType\n  thumbnail\n  videoUrl\n  badgeList\n  statusBadge\n  titleImage\n  title\n  altText\n  metaList\n  caption\n  scheme\n  seriesId\n  eventLog {\n    ...EventLogFragment\n  }\n  moreButton {\n    ...MoreButtonFragment\n  }\n  discountRate\n  discountRateText\n}\n    \n\n    fragment MoreButtonFragment on MoreButton {\n  type\n  scheme\n  title\n}\n    \n\n    fragment OnAirItem on OnAirItem {\n  thumbnail\n  videoUrl\n  titleImage\n  title\n  subtitleList\n  caption\n  scheme\n}\n    \n\n    fragment CardViewItem on CardViewItem {\n  title\n  altText\n  thumbnail\n  titleImage\n  scheme\n  badgeList\n  ageGradeBadge\n  statusBadge\n  ageGrade\n  selfCensorship\n  subtitleList\n  caption\n  rank\n  rankVariation\n  isEventBanner\n  categoryType\n  discountRate\n  discountRateText\n  eventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment CleanViewItem on CleanViewItem {\n  id\n  type\n  showPlayerIcon\n  scheme\n  title\n  thumbnail\n  badgeList\n  ageGradeBadge\n  statusBadge\n  subtitleList\n  rank\n  ageGrade\n  selfCensorship\n  eventLog {\n    ...EventLogFragment\n  }\n  discountRate\n  discountRateText\n}\n    \n\n    fragment PosterViewItem on PosterViewItem {\n  id\n  type\n  showPlayerIcon\n  scheme\n  title\n  altText\n  thumbnail\n  badgeList\n  labelBadgeList\n  ageGradeBadge\n  statusBadge\n  subtitleList\n  rank\n  rankVariation\n  ageGrade\n  selfCensorship\n  eventLog {\n    ...EventLogFragment\n  }\n  seriesId\n  showDimmedThumbnail\n  discountRate\n  discountRateText\n}\n    \n\n    fragment StrategyViewItem on StrategyViewItem {\n  id\n  title\n  count\n  scheme\n}\n    \n\n    fragment RankingListViewItem on RankingListViewItem {\n  title\n  thumbnail\n  badgeList\n  ageGradeBadge\n  statusBadge\n  ageGrade\n  selfCensorship\n  metaList\n  descriptionList\n  scheme\n  rank\n  eventLog {\n    ...EventLogFragment\n  }\n  discountRate\n  discountRateText\n}\n    \n\n    fragment NormalListViewItem on NormalListViewItem {\n  id\n  type\n  altText\n  ticketUid\n  thumbnail\n  badgeList\n  ageGradeBadge\n  statusBadge\n  ageGrade\n  isAlaramOn\n  row1\n  row2\n  row3 {\n    id\n    metaList\n  }\n  row4\n  row5\n  scheme\n  continueScheme\n  nextProductScheme\n  continueData {\n    ...ContinueInfoFragment\n  }\n  seriesId\n  isCheckMode\n  isChecked\n  isReceived\n  isHelixGift\n  price\n  discountPrice\n  discountRate\n  discountRateText\n  showPlayerIcon\n  rank\n  isSingle\n  singleSlideType\n  ageGrade\n  selfCensorship\n  eventLog {\n    ...EventLogFragment\n  }\n  giftEventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment ContinueInfoFragment on ContinueInfo {\n  title\n  isFree\n  productId\n  lastReadProductId\n  scheme\n  continueProductType\n  hasNewSingle\n  hasUnreadSingle\n}\n    \n\n    fragment MoreItem on MoreItem {\n  id\n  scheme\n  title\n}\n    \n\n    fragment EventBannerItem on EventBannerItem {\n  bannerType\n  thumbnail\n  videoUrl\n  titleImage\n  title\n  subtitleList\n  caption\n  scheme\n  eventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment LineBannerItem on LineBannerItem {\n  title\n  scheme\n  subTitle\n  bgColor\n  rightImage\n  eventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment Group on Group {\n  id\n  ... on ListViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n  ... on CardViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n  ... on PosterViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n  type\n  dataKey\n  groups {\n    ...GroupInGroup\n  }\n  items {\n    ...Item\n  }\n}\n    \n\n    fragment GroupInGroup on Group {\n  id\n  type\n  dataKey\n  items {\n    ...Item\n  }\n  ... on ListViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n  ... on CardViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n  ... on PosterViewGroup {\n    meta {\n      title\n      count\n    }\n  }\n}\n    ".trimIndent()

        val variables = mapOf(
            "sectionId" to "static-landing-Genre-section-Layout-10-0-update-false-82",
            "param" to mapOf(
                "categoryUid" to 10,
                "subcategoryUid" to "0",
                "sortType" to "update",
                "isComplete" to false,
                "screenUid" to 82,
                "page" to page
            )
        )

        val requestBody = mapOf("query" to query, "variables" to variables)
        val headers = createHeaders()

        val requestEntity = HttpEntity(requestBody, headers)
        val response = restTemplate.exchange(GRAPHQL_URL, HttpMethod.POST, requestEntity, String::class.java)
        val responseBody = response.body

        return runCatching {
            val parsedResponse = objectMapper.readValue(responseBody, GetStaticLandingGenreSectionResponse::class.java)
            listOf(parsedResponse)
        }.getOrElse { error ->
            println("❌ JSON 파싱 실패: ${error.message}")
            emptyList()
        }

    }

    fun fetchWebtoonDetails(siteWebtoonId: Long): Webtoon {
        val maxRetries = 3
        var attempt = 0

        while (attempt < maxRetries) {
            try {
                val seriesId = "\$seriesId"
                val query =
                    "\n    query contentHomeOverview($seriesId: Long!) {\n  contentHomeOverview(seriesId: $seriesId) {\n    id\n    seriesId\n    displayAd {\n      ...DisplayAd\n      ...DisplayAd\n    }\n    content {\n      ...SeriesFragment\n    }\n    displayAd {\n      ...DisplayAd\n    }\n    lastNoticeDate\n    moreButton {\n      type\n      scheme\n      title\n    }\n    setList {\n      ...NormalListViewItem\n    }\n    relatedSeries {\n      ...SeriesFragment\n    }\n  }\n}\n    \n    fragment DisplayAd on DisplayAd {\n  sectionUid\n  bannerUid\n  treviUid\n  momentUid\n}\n    \n\n    fragment SeriesFragment on Series {\n  id\n  seriesId\n  title\n  thumbnail\n  landThumbnail\n  categoryUid\n  category\n  categoryType\n  subcategoryUid\n  subcategory\n  badge\n  isAllFree\n  isWaitfree\n  ageGrade\n  state\n  onIssue\n  authors\n  description\n  pubPeriod\n  freeSlideCount\n  lastSlideAddedDate\n  waitfreeBlockCount\n  waitfreePeriodByMinute\n  bm\n  saleState\n  startSaleDt\n  saleMethod\n  discountRate\n  discountRateText\n  serviceProperty {\n    ...ServicePropertyFragment\n  }\n  operatorProperty {\n    ...OperatorPropertyFragment\n  }\n  assetProperty {\n    ...AssetPropertyFragment\n  }\n}\n    \n\n    fragment ServicePropertyFragment on ServiceProperty {\n  viewCount\n  readCount\n  ratingCount\n  ratingSum\n  commentCount\n  pageContinue {\n    ...ContinueInfoFragment\n  }\n  todayGift {\n    ...TodayGift\n  }\n  preview {\n    ...PreviewFragment\n    ...PreviewFragment\n  }\n  waitfreeTicket {\n    ...WaitfreeTicketFragment\n  }\n  isAlarmOn\n  isLikeOn\n  ticketCount\n  purchasedDate\n  lastViewInfo {\n    ...LastViewInfoFragment\n  }\n  purchaseInfo {\n    ...PurchaseInfoFragment\n  }\n  preview {\n    ...PreviewFragment\n  }\n  ticketInfo {\n    price\n    discountPrice\n    ticketType\n  }\n}\n    \n\n    fragment ContinueInfoFragment on ContinueInfo {\n  title\n  isFree\n  productId\n  lastReadProductId\n  scheme\n  continueProductType\n  hasNewSingle\n  hasUnreadSingle\n}\n    \n\n    fragment TodayGift on TodayGift {\n  id\n  uid\n  ticketType\n  ticketKind\n  ticketCount\n  ticketExpireAt\n  ticketExpiredText\n  isReceived\n  seriesId\n}\n    \n\n    fragment PreviewFragment on Preview {\n  item {\n    ...PreviewSingleFragment\n  }\n  nextItem {\n    ...PreviewSingleFragment\n  }\n  usingScroll\n}\n    \n\n    fragment PreviewSingleFragment on Single {\n  id\n  productId\n  seriesId\n  title\n  thumbnail\n  badge\n  isFree\n  ageGrade\n  state\n  slideType\n  lastReleasedDate\n  size\n  pageCount\n  isHidden\n  remainText\n  isWaitfreeBlocked\n  saleState\n  operatorProperty {\n    ...OperatorPropertyFragment\n  }\n  assetProperty {\n    ...AssetPropertyFragment\n  }\n}\n    \n\n    fragment OperatorPropertyFragment on OperatorProperty {\n  thumbnail\n  copy\n  helixImpId\n  isTextViewer\n  selfCensorship\n  cashInfo {\n    discountRate\n    setDiscountRate\n  }\n  ticketInfo {\n    price\n    discountPrice\n    ticketType\n  }\n}\n    \n\n    fragment AssetPropertyFragment on AssetProperty {\n  bannerImage\n  cardImage\n  cardTextImage\n  cleanImage\n  ipxVideo\n}\n    \n\n    fragment WaitfreeTicketFragment on WaitfreeTicket {\n  chargedPeriod\n  chargedCount\n  chargedAt\n}\n    \n\n    fragment LastViewInfoFragment on LastViewInfo {\n  isDone\n  lastViewDate\n  rate\n  spineIndex\n}\n    \n\n    fragment PurchaseInfoFragment on PurchaseInfo {\n  purchaseType\n  rentExpireDate\n  expired\n}\n    \n\n    fragment NormalListViewItem on NormalListViewItem {\n  id\n  type\n  altText\n  ticketUid\n  thumbnail\n  badgeList\n  ageGradeBadge\n  statusBadge\n  ageGrade\n  isAlaramOn\n  row1\n  row2\n  row3 {\n    id\n    metaList\n  }\n  row4\n  row5\n  scheme\n  continueScheme\n  nextProductScheme\n  continueData {\n    ...ContinueInfoFragment\n  }\n  seriesId\n  isCheckMode\n  isChecked\n  isReceived\n  isHelixGift\n  price\n  discountPrice\n  discountRate\n  discountRateText\n  showPlayerIcon\n  rank\n  isSingle\n  singleSlideType\n  ageGrade\n  selfCensorship\n  eventLog {\n    ...EventLogFragment\n  }\n  giftEventLog {\n    ...EventLogFragment\n  }\n}\n    \n\n    fragment EventLogFragment on EventLog {\n  fromGraphql\n  click {\n    layer1\n    layer2\n    setnum\n    ordnum\n    copy\n    imp_id\n    imp_provider\n  }\n  eventMeta {\n    id\n    name\n    subcategory\n    category\n    series\n    provider\n    series_id\n    type\n  }\n  viewimp_contents {\n    type\n    name\n    id\n    imp_area_ordnum\n    imp_id\n    imp_provider\n    imp_type\n    layer1\n    layer2\n  }\n  customProps {\n    landing_path\n    view_type\n    helix_id\n    helix_yn\n    helix_seed\n    content_cnt\n    event_series_id\n    event_ticket_type\n    play_url\n    banner_uid\n  }\n}\n    "
                val variables = mapOf("seriesId" to siteWebtoonId)
                val requestBody =
                    mapOf("query" to query, "operationName" to "contentHomeOverview", "variables" to variables)
                val headers = createHeaders()
                val requestEntity = HttpEntity(requestBody, headers)

                val response = restTemplate.exchange(GRAPHQL_URL, HttpMethod.POST, requestEntity, String::class.java)
                val responseBody = response.body

                return runCatching {
                    val parsedResponse =
                        objectMapper.readValue(responseBody, GetContentHomeOverviewResponse::class.java)
                    val content = parsedResponse.data.contentHomeOverview.content

                    Webtoon(
                        webtoonName = content.title,
                        platform = Platform.KAKAO_PAGE,
                        siteWebtoonId = siteWebtoonId,
                        webtoonLink = "https://page.kakao.com/content/$siteWebtoonId",
                        thumbnailUrl = content.thumbnail,
                        authors = content.authors,
                        finished = content.onIssue == "End"
                    )
                }.getOrElse { error ->
                    println("❌ JSON 파싱 실패: ${error.message}")
                    throw RuntimeException("웹툰 정보를 불러오는 데 실패했습니다.")
                }
            } catch (e: HttpServerErrorException.GatewayTimeout) {
                attempt++
                println("⚠️ 504 Gateway Time-out 발생 (시도: $attempt/$maxRetries)")
            }
        }
        throw RuntimeException("❌ 3번 시도 후에도 웹툰 정보를 가져오지 못했습니다.")
    }
}