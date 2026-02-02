import { Component, OnInit, ChangeDetectorRef, ViewChild, AfterViewInit } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { from, concatMap, of, tap, catchError, delay } from 'rxjs';
import { Destination } from '../../../../core/models/models';
import { DestinationsService } from '../../../../core/services/destinations.service';
import { ExternalApiService } from '../../../../core/services/external-api.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-admin-dashboard',
    templateUrl: './admin-dashboard.component.html',
    styleUrls: ['./admin-dashboard.component.css'],
    standalone: false
})
export class AdminDashboardComponent implements OnInit, AfterViewInit {
    private _suggestionsPaginator!: MatPaginator;
    @ViewChild('suggestionsPaginator') set suggestionsPaginator(value: MatPaginator) {
        this._suggestionsPaginator = value;
        if (this.suggestionsDataSource) this.suggestionsDataSource.paginator = value;
    }

    get suggestionsPaginator(): MatPaginator {
        return this._suggestionsPaginator;
    }

    suggestionColumns: string[] = ['select', 'flagUrl', 'country', 'capital', 'currency', 'population'];
    suggestionsDataSource = new MatTableDataSource<Destination>([]);
    selection = new SelectionModel<Destination>(true, []);

    internalColumns: string[] = ['select', 'flagUrl', 'country', 'capital', 'currency', 'population'];
    internalDataSource = new MatTableDataSource<Destination>([]);
    internalSelection = new SelectionModel<Destination>(true, []);

    isFetchingSuggestions = false;
    isSaving = false;
    isDeleting = false;

    progressValue = 0;
    totalToProcess = 0;

    currentView: 'browse' | 'manage' = 'browse';
    externalSearchKeyword = '';

    totalItems = 0;
    pageSize = 10;
    currentPage = 0;
    searchKeyword = '';

    constructor(
        private destinationsService: DestinationsService,
        private externalApiService: ExternalApiService,
        private authService: AuthService,
        private router: Router,
        private toastr: ToastrService,
        private cdr: ChangeDetectorRef
    ) { }

    ngOnInit(): void {
        // Use setTimeout to avoid NG0100 during initial load
        setTimeout(() => {
            this.fetchSuggestions();
            this.loadInternalDestinations();
        });
    }

    ngAfterViewInit() {
    }

    private chunkArray<T>(array: T[], size: number): T[][] {
        const chunks = [];
        for (let i = 0; i < array.length; i += size) {
            chunks.push(array.slice(i, i + size));
        }
        return chunks;
    }

    private getPageData(): Destination[] {
        if (!this.suggestionsPaginator) return this.suggestionsDataSource.data;
        const startIndex = this.suggestionsPaginator.pageIndex * this.suggestionsPaginator.pageSize;
        return this.suggestionsDataSource.filteredData.slice(startIndex, startIndex + this.suggestionsPaginator.pageSize);
    }

    onExternalSearch(event: Event): void {
        const keyword = (event.target as HTMLInputElement).value;
        if (this.externalSearchKeyword === keyword) return;
        this.externalSearchKeyword = keyword;

        if (keyword.length > 2 || keyword.length === 0) {
            setTimeout(() => {
                this.isFetchingSuggestions = true;
                this.cdr.markForCheck();
            });

            this.externalApiService.searchByName(keyword).subscribe({
                next: (data: Destination[]) => {
                    setTimeout(() => {
                        this.suggestionsDataSource.data = data;
                        this.isFetchingSuggestions = false;
                        this.cdr.markForCheck();
                    });
                },
                error: () => {
                    setTimeout(() => {
                        this.suggestionsDataSource.data = [];
                        this.isFetchingSuggestions = false;
                        this.cdr.markForCheck();
                    });
                }
            });
        }
    }

    fetchSuggestions(): void {
        setTimeout(() => {
            this.isFetchingSuggestions = true;
            this.cdr.markForCheck();
        });

        this.externalApiService.getCountrySuggestions().subscribe({
            next: (data: Destination[]) => {
                setTimeout(() => {
                    this.suggestionsDataSource.data = data;
                    this.isFetchingSuggestions = false;
                    this.cdr.markForCheck();
                }, 0);
            },
            error: () => {
                setTimeout(() => {
                    this.toastr.error('Failed to fetch suggestions');
                    this.isFetchingSuggestions = false;
                    this.cdr.markForCheck();
                }, 0);
            }
        });
    }

    loadInternalDestinations(): void {
        this.destinationsService.getDestinations(this.searchKeyword, this.currentPage, this.pageSize).subscribe({
            next: (res: any) => {
                setTimeout(() => {
                    this.internalDataSource.data = res.content;
                    this.totalItems = res.totalElements;
                    this.cdr.markForCheck();
                });
            },
            error: () => {
                setTimeout(() => {
                    this.toastr.error('Failed to load destinations');
                    this.cdr.markForCheck();
                });
            }
        });
    }

    bulkAdd(): void {
        const selected = [...this.selection.selected];
        if (selected.length === 0) return;

        setTimeout(() => {
            this.isSaving = true;
            this.progressValue = 0;
            this.totalToProcess = selected.length;
            this.cdr.markForCheck();

            const chunks = this.chunkArray(selected, 2);
            let processedCount = 0;

            from(chunks).pipe(
                concatMap(chunk => this.destinationsService.bulkCreate(chunk).pipe(
                    delay(500),
                    tap(() => {
                        processedCount += chunk.length;
                        this.progressValue = Math.round((processedCount / this.totalToProcess) * 100);
                        this.cdr.markForCheck();
                    }),
                    catchError(err => {
                        return of(null);
                    })
                ))
            ).subscribe({
                complete: () => {
                    setTimeout(() => {
                        this.toastr.success('added successfully');
                        this.selection.clear();
                        this.loadInternalDestinations();
                        this.isSaving = false;
                        this.progressValue = 0;
                        this.cdr.markForCheck();
                    }, 0);
                }
            });
        }, 0);
    }

    bulkDelete(): void {
        const selectedIds = [...this.internalSelection.selected.map(d => d.id!).filter(id => !!id)];
        if (selectedIds.length === 0) return;

        setTimeout(() => {
            this.isDeleting = true;
            this.progressValue = 0;
            this.totalToProcess = selectedIds.length;
            this.cdr.markForCheck();

            const chunks = this.chunkArray(selectedIds, 10);
            let processedCount = 0;

            from(chunks).pipe(
                concatMap(chunk => this.destinationsService.bulkDelete(chunk).pipe(
                    delay(300),
                    tap(() => {
                        processedCount += chunk.length;
                        this.progressValue = Math.round((processedCount / this.totalToProcess) * 100);
                        this.cdr.markForCheck();
                    }),
                    catchError(err => {
                        this.toastr.error(`Batch delete failed`);
                        return of(null);
                    })
                ))
            ).subscribe({
                complete: () => {
                    setTimeout(() => {
                        this.toastr.success('delete completed');
                        this.internalSelection.clear();
                        this.loadInternalDestinations();
                        this.isDeleting = false;
                        this.progressValue = 0;
                        this.cdr.markForCheck();
                    }, 0);
                }
            });
        }, 0);
    }

    isAllSuggestionsSelected() {
        const pageData = this.getPageData();
        return pageData.length > 0 && pageData.every(row => this.selection.isSelected(row));
    }

    masterToggleSuggestions() {
        const pageData = this.getPageData();
        if (this.isAllSuggestionsSelected()) {
            pageData.forEach(row => this.selection.deselect(row));
        } else {
            this.selection.clear();
            pageData.forEach(row => this.selection.select(row));
        }
    }

    isAllInternalSelected() {
        return this.internalDataSource.data.length > 0 &&
            this.internalDataSource.data.every(row => this.internalSelection.isSelected(row));
    }

    masterToggleInternal() {
        if (this.isAllInternalSelected()) {
            this.internalDataSource.data.forEach(row => this.internalSelection.deselect(row));
        } else {
            this.internalDataSource.data.forEach(row => this.internalSelection.select(row));
        }
    }

    applySearch(event: Event) {
        const val = (event.target as HTMLInputElement).value;
        if (this.searchKeyword === val) return;

        setTimeout(() => {
            this.searchKeyword = val;
            this.currentPage = 0;
            this.loadInternalDestinations();
            this.cdr.markForCheck();
        });
    }

    onPageChange(event: any) {
        this.currentPage = event.pageIndex;
        this.pageSize = event.pageSize;
        this.loadInternalDestinations();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/auth/login']);
    }
}
