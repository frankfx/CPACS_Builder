package de.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;

import de.business.TipicoTableFilterModel;
import de.business.TipicoTableModel;
import de.types.TipicoDataType;
import de.utils.Tupel;

public class FilterService {
	private static RowFilter<TipicoTableModel, Object> getCriteriaByTipicoDataType(TipicoTableFilterModel pModel) {
		// ID, TEAM, WINVALUE, EXPENSES, ATTEMPTS, DATE, SUCCESS
		switch (pModel.getFilterDataType()) {
		case TEAM:
			return RowFilter.regexFilter(pModel.getFilterValue(), TipicoDataType.TEAM.ordinal());
		case BET_PREDICTION:
			return RowFilter.regexFilter(pModel.getFilterValue(), TipicoDataType.BET_PREDICTION.ordinal());
		case WINVALUE:
			Tupel<ComparisonType, Float> tupel = pModel.getFilterOperationAsTupelComparisonTypeFloat();
			return RowFilter.numberFilter(tupel.getFirst(), tupel.getSecond(), TipicoDataType.WINVALUE.ordinal());
		case EXPENSES:
			Tupel<ComparisonType, Float> tupel2 = pModel.getFilterOperationAsTupelComparisonTypeFloat();
			return RowFilter.numberFilter(tupel2.getFirst(), tupel2.getSecond(), TipicoDataType.EXPENSES.ordinal());
		case DATE:
			Tupel<ComparisonType, LocalDate> tupel3 = pModel.getFilterOperationAsTupelComparisonTypeDate();
			return myLocalDateFilter(tupel3.getFirst(), tupel3.getSecond(), TipicoDataType.DATE.ordinal());
		default:
			return null;
		}
	}

	public static RowFilter<TipicoTableModel, Object> getCompleteCriteriaExpressionRec(List<?> pList) {
		if (pList != null && !pList.isEmpty())
			return getCompleteCriteriaExpressionRec(0, pList.size(), pList);
		return null;
	}

	private static RowFilter<TipicoTableModel, Object> getCompleteCriteriaExpressionRec(int curPos, int max, List<?> pList) {
		boolean isLastFilterRow = max <= curPos + 1;
		TipicoTableFilterModel lModel = (TipicoTableFilterModel) pList.get(curPos);

		RowFilter<TipicoTableModel, Object> curRowFilter = getCriteriaByTipicoDataType(lModel);

		if (isLastFilterRow) {
			return curRowFilter;
		} else {
			switch (lModel.getFilterConnectorType()) {
			case AND:
				List<RowFilter<TipicoTableModel, Object>> list = new ArrayList<RowFilter<TipicoTableModel, Object>>();
				list.add(curRowFilter);
				list.add(getCompleteCriteriaExpressionRec(curPos + 1, max, pList));
				return RowFilter.andFilter(list);
			case OR:
				list = new ArrayList<RowFilter<TipicoTableModel, Object>>();
				list.add(curRowFilter);
				list.add(getCompleteCriteriaExpressionRec(curPos + 1, max, pList));
				return RowFilter.orFilter(list);
			default:
				return null;
			}
		}
	}

	/**
	 * modification of Rowfilter.dateFilter to use LocalDate instead of util.Date
	 */
	@SuppressWarnings("unchecked")
	public static <M, I> RowFilter<M, I> myLocalDateFilter(ComparisonType type, LocalDate date, int... indices) {
		return (RowFilter<M, I>) new MyDateFilter(type, date, indices);
	}

	private static class MyDateFilter extends RowFilter<Object, Object> {
		private LocalDate date;
		private ComparisonType type;
		private int[] columns;

		MyDateFilter(ComparisonType type, LocalDate date, int[] columns) {
			if (type == null) {
				throw new IllegalArgumentException("type must be non-null");
			}
			checkIndices(columns);
			this.columns = columns;
			this.type = type;
			this.date = date;
		}

		public boolean include(Entry<? extends Object, ? extends Object> value) {
			int count = value.getValueCount();
			if (columns.length > 0) {
				for (int i = columns.length - 1; i >= 0; i--) {
					int index = columns[i];
					if (index < count) {
						if (include(value, index)) {
							return true;
						}
					}
				}
			} else {
				while (--count >= 0) {
					if (include(value, count)) {
						return true;
					}
				}
			}
			return false;
		}

		protected boolean include(Entry<? extends Object, ? extends Object> value, int index) {
			Object v = value.getValue(index);

			if (v instanceof LocalDate) {
				LocalDate vDate = (LocalDate) v;
				switch (type) {
				case BEFORE:
					return vDate.isBefore(date);
				case AFTER:
					return vDate.isAfter(date);
				case EQUAL:
					return vDate.equals(date);
				case NOT_EQUAL:
					return !vDate.equals(date);
				default:
					break;
				}
			}
			return false;
		}
	}

	private static void checkIndices(int[] columns) {
		for (int i = columns.length - 1; i >= 0; i--) {
			if (columns[i] < 0) {
				throw new IllegalArgumentException("Index must be >= 0");
			}
		}
	}
}
